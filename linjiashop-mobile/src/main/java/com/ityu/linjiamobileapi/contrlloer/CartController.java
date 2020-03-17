package com.ityu.linjiamobileapi.contrlloer;

import com.ityu.bean.entity.shop.Cart;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.bean.vo.shop.CartVo;
import com.ityu.service.shop.CartService;
import com.ityu.service.shop.GoodsService;
import com.ityu.service.shop.GoodsSkuService;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 11/5/2019 7:36 PM
 */
@RestController
@RequestMapping("/user/cart")
public class CartController extends BaseController {
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsSkuService goodsSkuService;
    @Autowired
    private GoodsService goodsService;
    @RequestMapping(value = "/queryByUser",method = RequestMethod.GET)
    public Object getByUser(){
        Long idUser = getAdminUser().getId();
        List<Cart> list = cartService.queryAll(SearchFilter.build("idUser", SearchFilter.Operator.EQ,idUser));
        return Rets.success(list);
    }
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object add(@RequestBody CartVo cartVo){
        Long idUser = getAdminUser().getId();
        cartVo.setIdUser(idUser);
        cartVo.setCount(1);
       Integer result = cartService.add(cartVo);
        return Rets.success(result);
    }
    @RequestMapping(value="/count",method = RequestMethod.GET)
    public Object count(){
        Long idUser = getAdminUser().getId();
        List<Cart> list = cartService.queryAll(SearchFilter.build("idUser", SearchFilter.Operator.EQ,idUser));
        return Rets.success(list.size());
    }

    @RequestMapping(value = "/update/{id}/{count}",method = RequestMethod.POST)
    public Object update(@PathVariable("id") Long id,
                          @PathVariable("count") String count){
        Cart cart = cartService.get(id);
        cart.setCount(new BigDecimal(count));
        cartService.update(cart);
        return Rets.success();
    }
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object remove(@RequestParam("id")  Long id){
        Long idUser = getAdminUser().getId();
        Cart cart = cartService.get(id);
        if(cart.getIdUser().intValue() == idUser.intValue()){
            cartService.delete(cart);
        }
        return Rets.success();
    }
}
