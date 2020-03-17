package com.ityu.linjiamobileapi.contrlloer;


import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.bean.entity.shop.Address;
import com.ityu.bean.entity.shop.Cart;
import com.ityu.bean.entity.shop.Order;
import com.ityu.bean.entity.shop.OrderItem;
import com.ityu.bean.enumeration.shop.OrderEnum;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.service.shop.AddressService;
import com.ityu.service.shop.CartService;
import com.ityu.service.shop.OrderService;
import com.ityu.utils.Lists;
import com.ityu.utils.Maps;
import com.ityu.utils.factory.Page;
import com.ityu.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 11/6/2019 5:07 PM
 */
@RestController
@RequestMapping("/user/order")
public class OrderController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderService orderService ;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressService addressService;
    @RequestMapping(value ="{orderSn}",method = RequestMethod.GET)
    public Object get(@PathVariable(value = "orderSn") String orderSn){
        Order order = orderService.getByOrderSn(orderSn);
        return Rets.success(order);
    }
    @RequestMapping(value = "getOrders",method = RequestMethod.GET)
    public Object getOrders(@RequestParam(value = "status",required = false) Integer status){
        Long idUser = getAdminUser().getId();
        Page<Order> page = new PageFactory<Order>().defaultPage();
        page.addFilter(SearchFilter.build("idUser", SearchFilter.Operator.EQ,idUser));
        page.setSort(Sort.by(Sort.Direction.DESC,"id"));
        if(status!=null &&status!=0){
            page.addFilter(SearchFilter.build("status", SearchFilter.Operator.EQ,status));
        }
        page = orderService.queryPage(page);
        return Rets.success(page);
    }

    @RequestMapping(value = "prepareCheckout",method = RequestMethod.GET)
    public Object prepareCheckout(@RequestParam(value = "chosenAddressId",required = false) Long chosenAddressId){
        Long idUser = getAdminUser().getId();
        List<Cart> list = cartService.queryAll(SearchFilter.build("idUser", SearchFilter.Operator.EQ,idUser));
        Address address = null;
        logger.info("chosenAddressId：{}",chosenAddressId);
        if(chosenAddressId==null || chosenAddressId==0) {
             address = addressService.getDefaultAddr(idUser);
        }else{
            address = addressService.get(chosenAddressId);
        }
        return Rets.success(Maps.newHashMap(
                "list",list,"addr",address
        ));
    }
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public Object save(
            @RequestParam("idAddress") Long idAddress,
            @RequestParam(value = "message",required = false) String message
    ){

        Long idUser = getAdminUser().getId();
        List<Cart> cartList = cartService.queryAll(SearchFilter.build("idUser", SearchFilter.Operator.EQ,idUser));
        Order order = new Order();
        order.setIdUser(idUser);
        order.setIdAddress(idAddress);
        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderItem> itemList  = Lists.newArrayList();
        for(Cart cart:cartList){
            OrderItem orderItem = new OrderItem();
            orderItem.setIdGoods(cart.getIdGoods());
            orderItem.setIdSku(cart.getIdSku());
            orderItem.setPrice(cart.getPrice());
            orderItem.setCount(cart.getCount());
            orderItem.setTotalPrice(orderItem.getPrice().multiply(orderItem.getCount()));
            totalPrice = totalPrice.add(orderItem.getTotalPrice());
            itemList.add(orderItem);
        }
        order.setMessage(message);
        order.setTotalPrice(totalPrice);
        order.setRealPrice(totalPrice);
        order.setStatus(OrderEnum.OrderStatusEnum.UN_PAY.getId());
        order.setPayStatus(OrderEnum.PayStatusEnum.UN_PAY.getId());
        orderService.save(order,itemList);
        cartService.deleteAll(cartList);
        return Rets.success(order);
    }
    @RequestMapping(value = "cancel/{orderSn}",method = RequestMethod.POST)
    public Object cancel(@PathVariable("orderSn") String orderSn){
        orderService.cancel(orderSn);
        return Rets.success();
    }
     @RequestMapping(value = "confirm/{orderSn}",method = RequestMethod.POST)
    public Object confirm(@PathVariable("orderSn") String orderSn){
        Order order = orderService.confirmReceive(orderSn);
        return Rets.success(order);
     }
     @RequestMapping(value = "payment/{orderSn}",method = RequestMethod.POST)
     public Object payment(@PathVariable("orderSn") String orderSn,
                           @RequestParam("payType") String payType){
         orderService.payment(orderSn,payType);
         return Rets.success();
     }
}
