package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.LoginLog
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.service.system.LoginLogService
import com.ityu.common.utils.BeanUtil
import com.ityu.common.utils.DateUtil
import com.ityu.common.utils.factory.Page
import com.ityu.common.warpper.LogWrapper
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

/**
 * 登录日志controller
 *
 * @author enilu
 * @version 2018/10/5 0005
 */
@Api(tags = ["loginLog"])
@Tag(name = "loginLog", description = "登录日志模块")
@RestController
@RequestMapping("/loginLog")
open class LoginLogController(private val loginlogService: LoginLogService) {


    @GetMapping(value = ["/list"])
   open fun list(
        @RequestParam(required = false) beginTime: String?,
        @RequestParam(required = false) endTime: String?,
        @RequestParam(required = false) logName: String?
    ): Any {
        val page: Page<LoginLog> = PageFactory<LoginLog>().defaultPage()
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parseDate(beginTime))
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parseDate(endTime))
        page.addFilter("logname", SearchFilter.Operator.LIKE, logName)
        val pageResult: Page<LoginLog> = loginlogService.queryPage(page)
        pageResult.setRecords(LogWrapper(BeanUtil.objectsToMaps(pageResult.records)).warp() as List<LoginLog?>)
        return Rets.success(pageResult)
    }

    /**
     * 清空日志
     */
    @DeleteMapping
    @BussinessLog(value = "清空登录日志")
    open fun clear(): Any {
        loginlogService!!.clear()
        return success()
    }
}
