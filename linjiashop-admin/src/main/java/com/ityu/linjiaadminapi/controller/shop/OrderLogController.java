package com.ityu.linjiaadminapi.controller.shop;

import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.core.BussinessLog;

import com.ityu.bean.entity.shop.OrderLog;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.service.shop.OrderLogService;
import com.ityu.utils.StringUtil;
import com.ityu.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/order/log")
public class OrderLogController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private OrderLogService orderLogService;
	@RequestMapping(value = "/queryByIdOrder/{idOrder}",method = RequestMethod.GET)
	public Object queryByIdOrder(@PathVariable("idOrder") Long idOrder){
		List<OrderLog> logList = orderLogService.queryAll(SearchFilter.build("idOrder",idOrder));
		return Rets.success(logList);
	}
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<OrderLog> page = new PageFactory<OrderLog>().defaultPage();
		page = orderLogService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑订单日志", key = "name")
	public Object save(@ModelAttribute OrderLog tShopOrderLog){
		if(tShopOrderLog.getId()==null){
			orderLogService.insert(tShopOrderLog);
		}else {
			orderLogService.update(tShopOrderLog);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除订单日志", key = "id")
	public Object remove(Long id){
		if (StringUtil.isEmpty(id)) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		orderLogService.deleteById(id);
		return Rets.success();
	}
}
