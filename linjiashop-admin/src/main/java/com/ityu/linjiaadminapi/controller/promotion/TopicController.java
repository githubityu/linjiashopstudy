package com.ityu.linjiaadminapi.controller.promotion;


import com.ityu.bean.constant.factory.PageFactory;

import com.ityu.bean.entity.promotion.Topic;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.core.BussinessLog;
import com.ityu.service.promotion.TopicService;
import com.ityu.utils.DateUtil;
import com.ityu.utils.StringUtil;
import com.ityu.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotion/topic")
public class TopicController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private TopicService topicService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list(@RequestParam(value = "disabled",required = false) Boolean disabled,
					   @RequestParam(value = "startDate",required = false) String startDate,
					   @RequestParam(value = "endDate",required = false) String endDate) {
		Page<Topic> page = new PageFactory<Topic>().defaultPage();
		page.addFilter( "disabled", disabled);
		page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate,"yyyyMMddHHmmss"));
		page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate,"yyyyMMddHHmmss"));
		page = topicService.queryPage(page);
		return Rets.success(page);
	}

	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑专题", key = "name")
	//@RequiresPermissions(value = {Permission.TOPIC_EDIT})
	public Object save(@ModelAttribute Topic topic){
		if(topic.getId()==null){
			topic.setPv(0L);
			topicService.insert(topic);
		}else {
			topicService.update(topic);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除专题", key = "id")
	//@RequiresPermissions(value = {Permission.TOPIC_DEL})
	public Object remove(Long id){
		if (StringUtil.isEmpty(id)) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		topicService.deleteById(id);
		return Rets.success();
	}

	@RequestMapping(value="/changeDisabled",method = RequestMethod.POST)
	//@RequiresPermissions(value = {Permission.TOPIC_EDIT})
	public Object changeIsOnSale(@RequestParam("id")  Long id, @RequestParam("disabled") Boolean disabled){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		topicService.changeDisabled(id,disabled);
		return Rets.success();
	}
}
