package com.ityu.linjiamobileapi.contrlloer;


import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.bean.vo.UserInfo;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.shop.WechatInfo;
import com.ityu.cache.CacheDao;
import com.ityu.cache.TokenCache;
import com.ityu.service.base.ApplicationProperties;
import com.ityu.service.shop.ShopUserService;
import com.ityu.utils.JwtTokenUtil;
import com.ityu.utils.RandomUtil;
import com.ityu.utils.StringUtil;
import com.ityu.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：enilu
 * @date ：Created in 11/5/2019 9:01 PM
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {
    @Autowired
    private ShopUserService shopUserService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private TokenCache tokenCache;

    @Autowired
    private CacheDao cacheDao;

    @Autowired
    private ApplicationProperties applicationProperties;

    private Logger logger = LoggerFactory.getLogger(LoginController.class);


    @ApiOperation(value = "发送验证码")
    @ApiImplicitParams({ // 参数说明
            @ApiImplicitParam(name = "mobile", paramType = "query", value = "手机号", dataType = "string", required = true),
    })
    @RequestMapping(value = "sendSmsCode", method = RequestMethod.POST)
    public Object sendSmsCode(@RequestParam String mobile) {
        if("15011112222".equals(mobile)){
            return Rets.success(shopUserService.sendSmsCodeForTest(mobile));
        }
        if ("prod".equals(applicationProperties.getEnv())) {
            //生产环境
            if(StringUtil.isMobile(mobile)) {
                Boolean ret = shopUserService.sendSmsCode(mobile);
                return Rets.success(ret);
            }else{
                return Rets.failure("非法的手机号");
            }
        } else {
            //测试环境
            String ret = shopUserService.sendSmsCodeForTest(mobile);
            return Rets.success(ret);
        }
    }

    /**
     * 使用手机号和短信验证码登录或者注册
     *
     * @param mobile
     * @param smsCode
     * @return
     */
    @ApiOperation(value = "登录或注册")
    @ApiImplicitParams({ // 参数说明
            @ApiImplicitParam(name = "mobile", paramType = "query", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "smsCode", paramType = "query", value = "验证码", dataType = "string", required = true),
    })
    @RequestMapping(value = "loginOrReg", method = RequestMethod.POST)
    public Object loginOrReg(@RequestParam String mobile, @RequestParam String smsCode) {
        try {
            logger.info("用户登录:" + mobile + ",短信验证码:" + smsCode);
            //1,
            ShopUser user = shopUserService.findByMobile(mobile);
            Boolean validateRet = shopUserService.validateSmsCode(mobile, smsCode);

            Map<String, Object> result = new HashMap<>(6);
            if (validateRet) {
                if (user == null) {
                    //初始化6位密码
                    String initPassword = RandomUtil.getRandomString(6);
                    user = shopUserService.register(mobile, initPassword);
                    result.put("initPassword", initPassword);
                }
                String token = JwtTokenUtil.getInstance().generateToken(user.getMobile(), user.getId());
                String sToken = JwtTokenUtil.TOKEN_PREFIX + token;
                user.setLastLoginTime(new Date());
                shopUserService.update(user);
                tokenCache.putToken(token, user.getId());
                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(user, userInfo);
                WechatInfo wechatInfo = cacheDao.hget(CacheDao.SESSION, "WECHAT_INFO" + user.getId(), WechatInfo.class);
                if (wechatInfo != null) {
                    userInfo.setRefreshWechatInfo(false);
                }
                result.put("user", userInfo);
                logger.info("token:{}", token);
                result.put("token", sToken);
                return Rets.success(result);
            }
            return Rets.failure("短信验证码错误");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }

    /**
     * 使用手机号和密码登录
     *
     * @param mobile
     * @param password
     * @return
     */
    @ApiOperation(value = "登录或注册")
    @ApiImplicitParams({ // 参数说明
            @ApiImplicitParam(name = "mobile", paramType = "query", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "password", paramType = "query", value = "密码", dataType = "string", required = true),
    })
    @RequestMapping(value = "loginByPass", method = RequestMethod.POST)
    public Object loginByPass(@RequestParam String mobile, @RequestParam String password) {
        try {
            logger.info("用户登录:" + mobile + ",密码:" + password);
            //1,
            ShopUser user = shopUserService.findByMobile(mobile);
            if (user == null) {
                return Rets.failure("该用户不存在");
            }
            //2,
            if (!encoder.matches(password, user.getPassword())) {
                return Rets.failure("输入的密码错误");
            }
            String token = JwtTokenUtil.getInstance().generateToken(user.getMobile(), user.getId());
            String sToken = JwtTokenUtil.TOKEN_PREFIX + token;
            tokenCache.putToken(token, user.getId());
            Map<String, Object> result = new HashMap<>(1);
            user.setLastLoginTime(new Date());
            shopUserService.update(user);
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);
            WechatInfo wechatInfo = cacheDao.hget(CacheDao.SESSION,"WECHAT_INFO"+user.getId(),WechatInfo.class);
            if(wechatInfo!=null){
                userInfo.setRefreshWechatInfo(false);
            }
            logger.info("token:{}", sToken);
            result.put("token", sToken);
            result.put("user", userInfo);
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
    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Object logout() {
        logger.info("处理额外的退出登录逻辑");
        return Rets.success();
    }
}
