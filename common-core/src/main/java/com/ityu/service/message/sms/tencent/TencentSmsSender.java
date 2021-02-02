package com.ityu.service.message.sms.tencent;


import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.ityu.bean.constant.CfgKey;
import com.ityu.service.message.sms.SmsSender;
import com.ityu.service.system.CfgService;
import com.ityu.utils.Lists;
import com.ityu.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class TencentSmsSender implements SmsSender {
    private Logger logger = LoggerFactory.getLogger(TencentSmsSender.class);
    @Autowired
    private CfgService cfgService;
    @Override
    public boolean sendSms(String tplCode, String receiver, LinkedHashMap params, String content) {
        Integer appid = Integer.valueOf( cfgService.getCfgValue(CfgKey.API_TENCENT_SMS_APPID));
        String appkey = cfgService.getCfgValue(CfgKey.API_TENCENT_SMS_APPKEY);
        String smsSign = cfgService.getCfgValue(CfgKey.API_TENCENT_SMS_SIGN);
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult result = null;
        try{
            if(StringUtil.isNotEmpty(tplCode)){
                ArrayList list = (ArrayList) Lists.newArrayList(params.values());
                result = ssender.sendWithParam("86", receiver,
                        Integer.valueOf(tplCode), list, smsSign, "", "");
            }else {
                //发送固定内容短信
                result = ssender.send(0, "86", receiver,
                        content, "", "");

            }
            logger.info("腾讯短信发送结果:{}",result.errMsg);
            return result.result == 0;
        } catch (Exception e) {
            logger.error("发送短信异常",e);
        }

        return false;
    }
}
