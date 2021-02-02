package com.ityu.linjiaadminapi.controller;


import com.ityu.bean.constant.state.ManagerStatus;
import com.ityu.bean.entity.system.User;
import com.ityu.bean.vo.front.Rets;
import com.ityu.cache.CacheDao;
import com.ityu.core.log.LogManager;
import com.ityu.core.log.LogTaskFactory;
import com.ityu.security.AdminUser;
import com.ityu.service.system.AccountService;
import com.ityu.service.system.ManagerService;
import com.ityu.utils.HttpUtil;
import com.ityu.utils.JwtTokenUtil;
import com.ityu.utils.Maps;
import com.ityu.utils.StringUtil;
import com.ityu.web.ApiConstants;
import com.ityu.web.controller.BaseController;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * AccountController
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@RestController
@RequestMapping("/api/account")
public class AccountController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AccountController.class);



    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Value("${jwt.token.expire.time}")
    private Long tokenExpireTime;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private CacheDao cacheDao;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object info() {
        AdminUser shiroUser = getAdminUser();
        if (shiroUser == null) {
            return Rets.failure("请登录");
        }
        if (shiroUser.getRoleList().size() == 0) {
            return Rets.failure("未配置权限");
        }
        Map map = Maps.newHashMap("name", shiroUser.getName(), "role", "admin", "roles", shiroUser.getRoleCodes());
        map.put("permissions", shiroUser.getUrls());
        Map profile = (Map) Mapl.toMaplist(shiroUser);
        profile.put("dept", shiroUser.getDeptName());
        profile.put("roles", shiroUser.getRoleNames());
        map.put("profile", profile);
        return Rets.success(map);
    }

    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public Object updatePwd(String oldPassword, String password, String rePassword) {
        try {
            if (StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)) {
                return Rets.failure("密码不能为空");
            }
            if (!password.equals(rePassword)) {
                return Rets.failure("新密码前后不一致");
            }
            //获取用户信息
            User user = managerService.get(getAdminUser().getId());
            //不能修改超级管理员密码
            if (ApiConstants.ADMIN_ACCOUNT.equals(user.getAccount())) {
                return Rets.failure("不能修改超级管理员密码");
            }

            if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                return Rets.failure("旧密码输入错误");
            }
            user.setPassword(bCryptPasswordEncoder.encode(password));
            managerService.update(user);
            //清空缓存
            cacheDao.hset(CacheDao.SESSION,user.getAccount(),null);
            return Rets.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("更改密码失败");
    }

    /**
     * 用户登录<br>
     * 1，验证没有注册<br>
     * 2，验证密码错误<br>
     * 3，登录成功
     *
     * @param mobile
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestParam("mobile") String mobile,
                        @RequestParam("password") String password) {
        try {
            logger.info("用户登录:" + mobile + ",密码:" + password);
            //1,
            User user = managerService.findByAccount(mobile);
            if (user == null) {
                return Rets.failure("该用户不存在");
            }
            if(user.getStatus() == ManagerStatus.FREEZED.getCode()){
                return Rets.failure("用户已冻结");
            }else if(user.getStatus() == ManagerStatus.DELETED.getCode()){
                return Rets.failure("用户已删除");
            }
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return Rets.failure("输入的密码错误");
            }
            String token = JwtTokenUtil.getInstance().generateToken(mobile,user.getId());
            String s = JwtTokenUtil.TOKEN_PREFIX + token;
            Map<String, String> result = new HashMap<>(1);
            logger.info("token:{}", s);
            result.put("token", s);
            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Object logout() {
        AdminUser shiroUser = getAdminUser();
        accountService.logout(shiroUser.getId());
        LogManager.me().executeLog(LogTaskFactory.exitLog(shiroUser.getId(), HttpUtil.getIp()));
        return Rets.success();
    }

}
