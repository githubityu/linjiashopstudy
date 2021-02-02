package com.ityu.linjiaadminapi.controller.cms;


import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.bean.entity.cms.Contacts;

import com.ityu.service.cms.ContactsService;
import com.ityu.utils.DateUtil;
import com.ityu.utils.factory.Page;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邀约信息管理
 */
@RestController
@RequestMapping("/api/contacts")
public class ContactsController extends BaseController {
    @Autowired
    private ContactsService contactsService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.CONTACTS})
    public Object list(@RequestParam(required = false) String userName,
                       @RequestParam(required = false) String mobile,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate

    ) {
        Page<Contacts> page = new PageFactory<Contacts>().defaultPage();
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate,"yyyyMMddHHmmss"));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate,"yyyyMMddHHmmss"));
        page.addFilter("userName", SearchFilter.Operator.EQ,userName);
        page.addFilter("mobile", SearchFilter.Operator.EQ,mobile);
        page = contactsService.queryPage(page);
        return Rets.success(page);
    }
}
