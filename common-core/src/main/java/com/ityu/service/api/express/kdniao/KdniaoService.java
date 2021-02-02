package com.ityu.service.api.express.kdniao;


import com.ityu.bean.constant.CfgKey;
import com.ityu.bean.entity.shop.ExpressInfo;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.cache.CacheDao;
import com.ityu.service.api.express.ExpressApi;
import com.ityu.service.system.CfgService;
import com.ityu.utils.Base64Util;
import com.ityu.utils.JsonUtil;
import com.ityu.utils.MD5;
import com.ityu.utils.Maps;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 快递鸟查询服务
 *
 * @author ：enilu
 * @date ：Created in 2020/5/31 10:27
 */
@Service
public class KdniaoService implements ExpressApi {
    private Logger logger = LoggerFactory.getLogger(KdniaoService.class);
    @Autowired
    private CfgService cfgService;
    @Autowired
    private CacheDao cacheDao;

    @Override
    public ExpressInfo realTimeQuery(String orderNo, String companyCode) {


        String url = cfgService.getCfgValue(CfgKey.API_KDNIAO_URL);
        String userId = cfgService.getCfgValue(CfgKey.API_KDNIAO_USERID);
        String apiKey = cfgService.getCfgValue(CfgKey.API_KDNIAO_APIKEY);
        logger.info("url:{}\nuserId:{}\napiKey:{}", url, userId, apiKey);
        Map appParams = Maps.newHashMap(
                "OrderCode", "",
                "ShipperCode", companyCode,
                "LogisticCode", orderNo
        );
        String jsonStr = JsonUtil.toJson(appParams);
        String datasign = null;
        try {
            datasign = URLEncoder.encode(Base64Util.base64Encode(MD5.getMD5String((jsonStr + apiKey)).toLowerCase().getBytes()));
            Map params = Maps.newHashMap(
                    "RequestType", "1002",
                    "EBusinessID", userId,
                    "RequestData", URLEncoder.encode(jsonStr, "UTF-8"),
                    "DataSign", datasign,
                    "DataType", "2"
            );
            Response response = Http.post2(url, params, 6000);
            logger.info(response.getContent());
            if (response.isOK()) {
                String content = response.getContent();
                KdniaoResponse kdniaoResponse = JsonUtil.fromJson(KdniaoResponse.class, content);
                ExpressInfo expressInfo = new ExpressInfo();
                List<Trace> list = kdniaoResponse.getTraces();
                Collections.reverse(list);
                expressInfo.setInfo(JsonUtil.toJson(list));
                int state = Integer.valueOf(kdniaoResponse.getState());
                switch (state){
                    case 2:
                        //-在途中,
                        expressInfo.setState(ExpressInfo.STATE_ING);
                        break;
                    case 3:
                        //-签收,
                        expressInfo.setState(ExpressInfo.STATE_FINISH);
                        break;
                    case 4:
                        //-问题件
                        expressInfo.setState(ExpressInfo.STATE_ERROR);
                        break;
                }

                return expressInfo;
            }
        } catch (Exception e) {
            throw new ApplicationException(BizExceptionEnum.SERVER_ERROR);
        }
        return null;

    }
}
