package com.ityu.linjiaadminapi.controller.message;


import com.ityu.bean.constant.factory.PageFactory;

import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.core.BussinessLog;
import com.ityu.bean.entity.message.MessageTemplate;

import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.service.message.MessagetemplateService;
import com.ityu.utils.factory.Page;
import com.ityu.bean.vo.front.Rets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/message/template")
public class MessagetemplateController {
    @Autowired
    private MessagetemplateService messagetemplateService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.MSG_TPL})
    public Object list() {
        Page<MessageTemplate> page = new PageFactory<MessageTemplate>().defaultPage();
        page = messagetemplateService.queryPage(page);
        page.setRecords(page.getRecords());
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑消息模板", key = "name")
    //@RequiresPermissions(value = {Permission.MSG_TPL_EDIT})
    public Object save(@ModelAttribute @Valid MessageTemplate messageTemplate) {
        if(messageTemplate.getId()==null){
            MessageTemplate old = messagetemplateService.get(SearchFilter.build("code",messageTemplate.getCode()));
            if(old!=null){
                return Rets.failure("模板编码已被使用");
            }
            messagetemplateService.insert(messageTemplate);
        }else {
            messagetemplateService.update(messageTemplate);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除消息模板", key = "id")
    //@RequiresPermissions(value = {Permission.MSG_TPL_DEL})
    public Object remove(Long id) {
        if (id==null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        messagetemplateService.deleteById(id);
        return Rets.success();
    }
}
