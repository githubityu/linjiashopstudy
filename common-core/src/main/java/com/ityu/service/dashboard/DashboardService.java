package com.ityu.service.dashboard;


import com.ityu.bean.enumeration.shop.OrderEnum;
import com.ityu.service.shop.CartService;
import com.ityu.service.shop.OrderService;
import com.ityu.service.shop.ShopUserService;
import com.ityu.utils.Maps;
import com.ityu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ：enilu
 * @date ：Created in 1/7/2020 2:25 PM
 */
@Service
public class DashboardService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private CartService cartService;
    public Map getDashboardData(){
        long orderCount = orderService.count();
        long userCount = shopUserService.count();
        long cartCount = cartService.count();
        Object orderSumPrice = orderService.getMapBySql("select sum(real_price) from t_shop_order where status<>"+ OrderEnum.OrderStatusEnum.UN_PAY.getId());
        Map result = Maps.newHashMap(
                "orderCount",orderCount,
                "userCount",userCount,
                "cartCount",cartCount,
                "orderSumPrice", StringUtil.isNotNullOrEmpty(orderSumPrice)?Double.valueOf(orderSumPrice.toString())/100:"0"
        );
        return result;
    }
}
