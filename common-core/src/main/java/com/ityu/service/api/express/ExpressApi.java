package com.ityu.service.api.express;


import com.ityu.bean.entity.shop.ExpressInfo;

/**
 * @author ：enilu
 * @date ：Created in 2021/1/2 23:13
 */
public interface ExpressApi {
    /**
     * 查询快递实时信息
     * @param orderNo 快递单号
     * @param companyCode 快递公司编码
     * @return
     */
    ExpressInfo realTimeQuery(String orderNo, String companyCode) ;
}
