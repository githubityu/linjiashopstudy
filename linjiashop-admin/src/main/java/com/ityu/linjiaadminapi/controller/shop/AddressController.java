package com.ityu.linjiaadminapi.controller.shop;


import com.ityu.bean.constant.factory.PageFactory;

import com.ityu.bean.entity.shop.Address;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.core.BussinessLog;
import com.ityu.service.shop.AddressService;
import com.ityu.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/address")
public class AddressController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AddressService addressService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list(@RequestParam(required = false)Long idUser) {
		Page<Address> page = new PageFactory<Address>().defaultPage();
		page.addFilter("idUser",idUser);
		page = addressService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑收货地址", key = "name")
	public Object save(@ModelAttribute Address tShopAddress){
		if(tShopAddress.getId()==null){
			addressService.insert(tShopAddress);
		}else {
			addressService.update(tShopAddress);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除收货地址", key = "id")
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		addressService.deleteById(id);
		return Rets.success();
	}
}
