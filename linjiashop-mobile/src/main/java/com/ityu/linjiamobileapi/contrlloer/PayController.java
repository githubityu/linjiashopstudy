package com.ityu.linjiamobileapi.contrlloer;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.ityu.bean.entity.shop.Order;
import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.bean.enumeration.shop.OrderEnum;
import com.ityu.bean.vo.front.Rets;
import com.ityu.linjiamobileapi.service.WeixinPayService;
import com.ityu.service.shop.OrderService;
import com.ityu.service.shop.ShopUserService;
import com.ityu.utils.StringUtil;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：enilu
 * @date ：Created in 2020/3/17 20:15
 */
@RestController
@RequestMapping("/pay")
public class PayController extends BaseController {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "wx/prepare", method = RequestMethod.POST)
    public Object wxPrepare(@RequestParam("orderSn") String orderSn) {
        ShopUser user = shopUserService.getCurrentUser();
        if (StringUtil.isEmpty(user.getWechatOpenId())) {
            return Rets.failure("非微信用户");
        }
        Order order = orderService.getByOrderSn(orderSn);
        WxPayMpOrderResult wxOrder = weixinPayService.prepare(user, order);
        if (wxOrder != null) {
            return Rets.success(wxOrder);
        }
        return Rets.failure("数据准备异常");
    }

    /**
     * 微信支付回调
     *
     * @return
     */
    @RequestMapping(value = "wx/notify", method = RequestMethod.POST)
    public Object wxNotify() {
        String msg = weixinPayService.resultNotify();
        return msg;
    }

    /**
     * 查询支付结果
     *
     * @param orderSn
     * @return
     */
    @RequestMapping(value = "queryResult/{orderSn}", method = RequestMethod.GET)
    public Object wxNotify(@PathVariable("orderSn") String orderSn) {
        Order order = orderService.getByOrderSn(orderSn);
        Boolean payResult = OrderEnum.PayStatusEnum.UN_SEND.getId().equals(order.getPayStatus())
                && OrderEnum.PayStatusEnum.UN_SEND.getId().equals(order.getStatus());
        return Rets.success(payResult);
    }
}

