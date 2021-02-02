package com.ityu.linjiamobileapi.contrlloer;


import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.bean.vo.UserInfo;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.shop.WechatInfo;
import com.ityu.cache.CacheDao;
import com.ityu.security.UserService;
import com.ityu.service.api.WeixinService;
import com.ityu.service.shop.ShopUserService;
import com.ityu.utils.Maps;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author ：enilu
 * @date ：Created in 2020/6/6 20:32
 */
@RestController
@RequestMapping("/wechat")
public class WeChatController extends BaseController {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private WeixinService weixinService;
    @Autowired
    private CacheDao cacheDao;

    @RequestMapping(value = "getWxOpenId", method = RequestMethod.POST)
    public Object getWxOpenId(String code, HttpServletRequest request) {
        WechatInfo wechatInfo = weixinService.getWechatInfoByCode(code);
        if (wechatInfo == null) {
            return Rets.failure("获取微信消息失败");
        }
        ShopUser user = shopUserService.findByWechatOpenId(wechatInfo.getOpenId());
        if(user==null) {
            user = shopUserService.getCurrentUser();
            if(user!=null){
                user.setWechatOpenId(wechatInfo.getOpenId());
                user.setWechatHeadImgUrl(wechatInfo.getHeadUrl());
                user.setNickName(wechatInfo.getNickName());
                shopUserService.update(user);
            }
        }
        if (user == null) {
            user = shopUserService.registerByWechatInfo(wechatInfo);
        }
        String token = userService.loginForToken(user.getMobile(), user.getId());
        user.setLastLoginTime(new Date());
        shopUserService.update(user);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user,userInfo);
        userInfo.setRefreshWechatInfo(false);
        Map result = Maps.newHashMap(
                "user",userInfo,
                "token",token
        );
        return Rets.success(result);
    }

    @RequestMapping(value = "getWxSign", method = RequestMethod.POST)
    public Object getWxSign(@RequestParam("url") String url) {
        Map<String, String> map = weixinService.getSign(url);
        return Rets.success(map);
    }
}
