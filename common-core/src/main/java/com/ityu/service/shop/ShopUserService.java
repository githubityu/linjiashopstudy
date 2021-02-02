package com.ityu.service.shop;


import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.enumeration.MessageTemplateEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.shop.WechatInfo;
import com.ityu.cache.CacheDao;
import com.ityu.dao.shop.ShopUserRepository;
import com.ityu.security.UserService;
import com.ityu.service.base.BaseService;
import com.ityu.service.message.MessageService;
import com.ityu.utils.HttpUtil;
import com.ityu.utils.Maps;
import com.ityu.utils.RandomUtil;
import com.ityu.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShopUserService extends BaseService<ShopUser, Long, ShopUserRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ShopUserRepository shopUserRepository;
    @Autowired
    private CacheDao cacheDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    BCryptPasswordEncoder encoder;

    public ShopUser findByMobile(String mobile) {
        return shopUserRepository.findByMobile(mobile);
    }

    public Boolean validateSmsCode(String mobile, String smsCode) {
        //todo 测试验证逻辑，暂不实现
        String key = mobile+"_smsCode";
        String smsCode2 = (String) cacheDao.hget(CacheDao.HOUR,key);
        return StringUtil.equals(smsCode,smsCode2);
    }

    public boolean  sendSmsCode(String mobile) {
        String smsCode = RandomUtil.getRandomNumber(4);
        String key =  mobile+"_smsCode";
        String timesKey = key+"_times";
        String oldSmsCode = (String) cacheDao.hget(CacheDao.MINUTE,key);
        if(StringUtil.isNotEmpty(oldSmsCode)){
            logger.info("{}:一分钟内已经发送过短信验证码，不再重复发送",oldSmsCode);
            throw  new ApplicationException(BizExceptionEnum.REQUEST_TOO_MANY);
        }
        Integer sendTimes =  cacheDao.hget(CacheDao.DAY,timesKey,Integer.class);
        sendTimes = sendTimes==null?0:sendTimes;
        if(sendTimes!=null&&sendTimes>10){
            logger.info("{}:当天发送短信验证码次数超限",mobile);
            throw  new ApplicationException(BizExceptionEnum.REQUEST_TOO_MANY);
        }
        cacheDao.hset(CacheDao.HOUR,key,smsCode);
        cacheDao.hset(CacheDao.DAY,timesKey,sendTimes++);
        logger.info("短信验证码:{}",smsCode);
        messageService.sendSms(MessageTemplateEnum.REGISTER_CODE.getCode(),mobile, Maps.newLinkedHashMap("code",smsCode));
        return true;
    }

    /**
     * 该方法仅作测试用<br>
     * 该方法不真正发送短信<br>
     * 该方法回将短信验证码返回给前端供用户输入
     * @param mobile
     * @return
     */
    public String  sendSmsCodeForTest(String mobile) {
        String smsCode = RandomUtil.getRandomNumber(4);
        String key =  mobile+"_smsCode";
        String timesKey = key+"_times";
        String oldSmsCode = (String) cacheDao.hget(CacheDao.MINUTE,key);
        if(StringUtil.isNotEmpty(oldSmsCode)){
            logger.info("{}:一分钟内已经发送过短信验证码，不再重复发送",oldSmsCode);
            throw  new ApplicationException(BizExceptionEnum.REQUEST_TOO_MANY);
        }
        Integer sendTimes =  cacheDao.hget(CacheDao.DAY,timesKey,Integer.class);
        sendTimes = sendTimes==null?0:sendTimes;
        if(sendTimes!=null&&sendTimes>500){
            logger.info("{}:当天发送短信验证码次数超限",mobile);
            throw  new ApplicationException(BizExceptionEnum.REQUEST_TOO_MANY);
        }
        cacheDao.hset(CacheDao.HOUR,key,smsCode);
        cacheDao.hset(CacheDao.DAY,timesKey,sendTimes++);
        logger.info("短信验证码:{}",smsCode);
        return smsCode;
    }

    public String sendSmsCodeForOldMobile(String mobile) {
        //todo 发送短信验证码逻辑，暂不实现
        String smsCode = RandomUtil.getRandomNumber(4);
        cacheDao.hset(CacheDao.SESSION,mobile+"_smsCode",smsCode);
        HttpUtil.getRequest().getSession().setAttribute(mobile+"_smsCode",smsCode);
        return smsCode;
    }


    public ShopUser register(String mobile, String initPwd) {
        ShopUser user = new ShopUser();
        user.setMobile(mobile);
        user.setNickName(mobile);
        user.setGender("male");
        user.setCreateTime(new Date());
        user.setSalt(RandomUtil.getRandomString(5));
        user.setPassword(encoder.encode(initPwd));

        insert(user);
        return user;
    }

    public ShopUser getCurrentUser() {
        return get(UserService.me().getTokenFromRequest().getId());
    }

    public ShopUser findByWechatOpenId(String openId) {
        return shopUserRepository.findByWechatOpenId(openId);
    }

    public ShopUser registerByWechatInfo(WechatInfo wechatInfo) {
        ShopUser user = new ShopUser();
        user.setWechatOpenId(wechatInfo.getOpenId());
        user.setWechatHeadImgUrl(wechatInfo.getHeadUrl());
        user.setNickName(wechatInfo.getNickName());
        user.setCreateTime(new Date());
        insert(user);
        return user;
    }
}

