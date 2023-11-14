package com.ityu.sdf.api.controller.cms

import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.entity.cms.Contacts
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.service.cms.ContactsService
import com.ityu.common.utils.DateUtil
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 邀约信息管理
 */
@Api(tags = ["contacts"])
@Tag(name = "contacts", description = "邀约信息管理")
@RestController
@RequestMapping("/contacts")
open class ContactsController(private val contactsService: ContactsService) {

    @GetMapping(value = ["/list"])
    open fun list(
        @RequestParam(required = false) userName: String?,
        @RequestParam(required = false) mobile: String?,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): Any {
        var page = PageFactory<Contacts?>().defaultPage()
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"))
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"))
        page.addFilter("userName", SearchFilter.Operator.EQ, userName)
        page.addFilter("mobile", SearchFilter.Operator.EQ, mobile)
        page = contactsService!!.queryPage(page)
        return success(page)
    }
}
