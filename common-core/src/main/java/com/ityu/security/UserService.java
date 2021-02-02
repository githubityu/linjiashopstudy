package com.ityu.security;


import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.bean.entity.system.Role;
import com.ityu.bean.entity.system.User;
import com.ityu.cache.CacheDao;
import com.ityu.cache.TokenCache;
import com.ityu.dao.shop.ShopUserRepository;
import com.ityu.dao.system.MenuRepository;
import com.ityu.dao.system.RoleRepository;
import com.ityu.dao.system.UserRepository;

import com.ityu.utils.Convert;
import com.ityu.utils.HttpUtil;
import com.ityu.utils.JwtTokenUtil;
import com.ityu.utils.StringUtil;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.ityu.bean.vo.SpringContextHolder.getBean;
import static com.ityu.utils.Contans.ROLE_PRE;


@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopUserRepository shopUserRepository;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenCache tokenCache;

    @Value("${jwt.user.type}")
    private Integer userType;

    @Value("${jwt.token.expire.time}")
    private Long tokenExpireTime;

    @Autowired
    private CacheDao cacheDao;


    public static UserService me() {
        return getBean(UserService.class);
    }

    public AdminUser adminUserForFilter(String token) {
        Pair<String, Long> user = JwtTokenUtil.getInstance().getUserFromToken(token);
        String token1 = tokenCache.getToken(user.getValue());
        if (token.equals(token1)) {
            AdminUser adminUser = adminUser(user.getKey());
            tokenCache.setUser(token, adminUser);
            return adminUser;
        } else {
            tokenCache.removeToken(user.getValue());
            return null;
        }
    }

    public AdminUser adminUserByToken(String token) {
        return tokenCache.getUser(token);
    }

    public AdminUser getTokenFromRequest() {
        try {
            String token = HttpUtil.getToken();
            if (token != null) {
                return adminUserByToken(token);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    //只有登录或者授权以后才会调用该方法 ,其他地方禁止调用
    public AdminUser adminUser(String username) {
        AdminUser shiroUser;
        if (userType == AdminUser.MANAGER) {
            User user = userRepository.findByAccount(username);
            if (user == null) {
                throw new UsernameNotFoundException("该用户不存在");
            }
            Long[] roleArray = Convert.toLongArray(",", user.getRoleid());
            List<Long> roleList = new ArrayList<>();
            List<String> roleNameList = new ArrayList<>();
            List<String> roleCodeList = new ArrayList<>();
            Set<String> permissions = new HashSet<>();
            Set<String> resUrls = new HashSet<>();
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
            for (Long roleId : roleArray) {
                roleList.add(roleId);
                Role role = roleRepository.getOne(roleId);
                roleNameList.add(role.getName());
                // authorities.add(new SimpleGrantedAuthority(ROLE_PRE+role));
                //@PreAuthorize("hasAuthority('ROLE_XYZ')")与@PreAuthorize("hasRole('XYZ')")相同
                simpleGrantedAuthorities.add(new SimpleGrantedAuthority(ROLE_PRE + role.getTips()));
                roleCodeList.add(role.getTips());
                // 资源的权限标识采用menu中的code和url，建议统一以url为准
                permissions.addAll(menuRepository.getResCodesByRoleId(roleId));
                List<String> list = menuRepository.getResUrlsByRoleId(roleId);
                for (String resUrl : list) {
                    if (StringUtil.isNotEmpty(resUrl)) {
                        resUrls.add(resUrl);
                    }
                }
            }
            shiroUser = new AdminUser(username, user.getPassword(), simpleGrantedAuthorities);
            shiroUser.setId(Long.valueOf(user.getId()));            // 账号id
            shiroUser.setDeptId(user.getDeptid());    // 部门id
            //shiroUser.setDeptName(ConstantFactory.me().getDeptName(user.getDeptid()));// 部门名称
            shiroUser.setName(user.getName());        // 用户名称
            shiroUser.setRoleList(roleList);
            shiroUser.setRoleNames(roleNameList);
            shiroUser.setRoleCodes(roleCodeList);
            shiroUser.setPermissions(permissions);
            shiroUser.setUrls(resUrls);
        } else {
            ShopUser user = shopUserRepository.findByMobile(username);
            if (user == null) {
                throw new UsernameNotFoundException("该用户不存在");
            }
            shiroUser = new AdminUser(username, user.getPassword(), new HashSet<>());
            shiroUser.setName(user.getNickName());
            shiroUser.setId(user.getId());
        }
        return shiroUser;
    }


    /**
     * 获取新的token
     * @return
     */
    public String  refreshToken(){
        //获取用户信息
        String oldToken = HttpUtil.getToken();
        AdminUser userBean = tokenCache.getUser(oldToken);
        //验证refreshToken是否有效
        if(refreshTokenIsValid(oldToken)) {
            //生成新token 返回界面
            String newToken = loginForToken(userBean.getUsername(),userBean.getId());
            return newToken;
        }
        return null;
    }

    public boolean refreshTokenIsValid(String token){
        String  refreshTokenTime = (String) cacheDao.hget(CacheDao.SESSION,token);
        if(refreshTokenTime == null){
            return false;
        }
        return System.currentTimeMillis()<=Long.valueOf(refreshTokenTime);

    }

    public String loginForToken(String account,Long id){
        //获取用户token值
        String token = JwtTokenUtil.getInstance().generateToken(account,id,tokenExpireTime*60000);
        //将token作为RefreshToken Key 存到缓存中，缓存时间为token有效期的两倍
        String   refreshTokenCacheKey = token;
        Date expireDate = new Date(System.currentTimeMillis()+tokenExpireTime*120000);
        cacheDao.hset(CacheDao.SESSION,refreshTokenCacheKey,String.valueOf(expireDate.getTime()));
        return token;
    }

}
