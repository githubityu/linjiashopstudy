package com.ityu.linjiaadminapi.controller.shop;

import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.core.BussinessLog;

import com.ityu.bean.entity.shop.OrderItem;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.service.shop.OrderItemService;
import com.ityu.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/order/item")
public class OrderItemController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private OrderItemService orderItemService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<OrderItem> page = new PageFactory<OrderItem>().defaultPage();
		page = orderItemService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑订单明细", key = "name")
	public Object save(@ModelAttribute OrderItem tShopOrderItem){
		if(tShopOrderItem.getId()==null){
			orderItemService.insert(tShopOrderItem);
		}else {
			orderItemService.update(tShopOrderItem);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除订单明细", key = "id")
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		orderItemService.deleteById(id);
		return Rets.success();
	}
}
