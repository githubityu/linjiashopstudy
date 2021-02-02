package com.ityu.linjiamobileapi.contrlloer;

import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.bean.entity.system.FileInfo;
import com.ityu.bean.vo.UserInfo;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.shop.WechatInfo;
import com.ityu.cache.CacheDao;
import com.ityu.service.api.WeixinService;
import com.ityu.service.shop.ShopUserService;
import com.ityu.service.system.FileService;
import com.ityu.utils.MD5;
import com.ityu.utils.StringUtil;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ：enilu
 * @date ：Created in 11/6/2019 4:20 PM
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private WeixinService weixinService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CacheDao cacheDao;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public Object getInfo() {
        Long idUser = getAdminUser().getId();
        ShopUser shopUser = shopUserService.get(idUser);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(shopUser, userInfo);
        WechatInfo wechatInfo = cacheDao.hget(CacheDao.SESSION, "WECHAT_INFO" + shopUser.getId(), WechatInfo.class);
        if (wechatInfo != null) {
            userInfo.setRefreshWechatInfo(false);
        }
        return Rets.success(userInfo);
    }

    @RequestMapping(value = "/updateUserName/{userName}", method = RequestMethod.POST)
    public Object updateUserName(@PathVariable("userName") String userName) {
        ShopUser user = shopUserService.getCurrentUser();
        user.setNickName(userName);
        shopUserService.update(user);
        return Rets.success(user);
    }

    @RequestMapping(value = "/updateGender/{gender}", method = RequestMethod.POST)
    public Object updateGender(@PathVariable("gender") String gender) {
        ShopUser user = shopUserService.getCurrentUser();
        user.setGender(gender);
        shopUserService.update(user);
        return Rets.success(user);
    }

    @RequestMapping(value = "/updatePassword/{oldPwd}/{password}/{rePassword}", method = RequestMethod.POST)
    public Object updatePassword(@PathVariable("oldPwd") String oldPwd,
                                 @PathVariable("password") String password,
                                 @PathVariable("rePassword") String rePassword) {
        if (StringUtil.isEmpty(oldPwd) || StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)) {
            return Rets.failure("项目并能为空");
        }
        if (!StringUtil.equals(password, rePassword)) {
            return Rets.failure("密码前后不一致");
        }
        ShopUser user = shopUserService.getCurrentUser();


        if (!bCryptPasswordEncoder.matches(oldPwd, user.getPassword())) {
            return Rets.failure("旧密码输入错误");
        }
        user.setPassword(bCryptPasswordEncoder.encode(password));
        shopUserService.update(user);
        return Rets.success();
    }

    @RequestMapping(value = "/updatePassword_v2/{password}/{smsCode}", method = RequestMethod.POST)
    public Object updatePassword(@PathVariable("password") String password,
                                 @PathVariable("smsCode") String smsCode) {
        ShopUser user = shopUserService.getCurrentUser();
        Boolean validateRet = shopUserService.validateSmsCode(user.getMobile(), smsCode);
        if (validateRet) {
            user.setPassword(bCryptPasswordEncoder.encode(password));
            shopUserService.update(user);
            return Rets.success();
        }
        return Rets.failure("短信验证码错误");

    }

    @RequestMapping(value = "sendSmsCode", method = RequestMethod.POST)
    public Object sendSmsCode(@RequestParam String mobile) {
        String smsCode = shopUserService.sendSmsCodeForOldMobile(mobile);
        //todo 测试环境直接返回验证码，生成环境切忌返回该验证码
        return Rets.success(smsCode);
    }


    @RequestMapping(value = "getWxOpenId", method = RequestMethod.POST)
    public Object getWxOpenId(String code, HttpServletRequest request) {
        ShopUser user = shopUserService.getCurrentUser();
        boolean wxAuth = weixinService.isAuth(user, code);
        return wxAuth ? Rets.success() : Rets.failure("获取openid失败");
    }

    @RequestMapping(value = "getWxSign", method = RequestMethod.POST)
    public Object getWxSign(@RequestParam("url") String url) {
        Map<String, String> map = weixinService.getSign(url);
        return Rets.success(map);
    }

    /**
     * 获取微信token，
     * todo 该方法仅用作测试,微信token会通过后台管理中的定时任务定时更新
     *
     * @return
     */
    @RequestMapping(value = "updateWxToken", method = RequestMethod.GET)
    public Object updateWxToken() {
        weixinService.updateWeixinToken();
        return Rets.success();

    }

    @RequestMapping(value = "uploadAvatar", method = RequestMethod.POST)
    public Object uploadAvatar(@RequestPart("file") MultipartFile multipartFile) {

        try {
            FileInfo fileInfo = fileService.upload(multipartFile);
            ShopUser user = shopUserService.getCurrentUser();
            user.setAvatar(String.valueOf(fileInfo.getRealFileName()));
            shopUserService.update(user);
            return Rets.success(fileInfo);
        } catch (Exception e) {
            logger.error("上传头像失败", e);
            return Rets.failure("上传头像失败");
        }
    }
}
