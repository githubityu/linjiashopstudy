package com.ityu.linjiaadminapi.controller.shop;

import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.core.BussinessLog;

import com.ityu.bean.entity.shop.Cart;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.service.shop.CartService;
import com.ityu.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/cart")
public class CartController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CartService cartService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<Cart> page = new PageFactory<Cart>().defaultPage();
		page = cartService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑购物车", key = "name")
	public Object save(@ModelAttribute Cart tShopCart){
		if(tShopCart.getId()==null){
			cartService.insert(tShopCart);
		}else {
			cartService.update(tShopCart);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除购物车", key = "id")
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		cartService.deleteById(id);
		return Rets.success();
	}
}
