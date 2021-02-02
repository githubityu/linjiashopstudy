package com.ityu.linjiaadminapi.controller.message;


import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.core.BussinessLog;
import com.ityu.bean.entity.message.Message;
import com.ityu.service.message.MessageService;
import com.ityu.utils.DateUtil;
import com.ityu.utils.factory.Page;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.MSG})
    public Object list(  @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) String endDate) {
        Page<Message> page = new PageFactory<Message>().defaultPage();
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate,"yyyyMMddHHmmss"));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate,"yyyyMMddHHmmss"));
        page = messageService.queryPage(page);
        page.setRecords(page.getRecords());
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "清空所有历史消息")
    //@RequiresPermissions(value = {Permission.MSG_CLEAR})
    public Object clear() {
        messageService.clear();
        return Rets.success();
    }
}