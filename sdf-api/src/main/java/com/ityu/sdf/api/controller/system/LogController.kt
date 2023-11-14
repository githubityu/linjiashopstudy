package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.constant.state.BizLogType
import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.OperationLog
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.service.system.OperationLogService
import com.ityu.common.utils.*
import com.ityu.common.utils.factory.Page
import com.ityu.common.warpper.LogWrapper
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

/**
 * LogController
 *
 * @author enilu
 * @version 2018/10/5 0005
 */
@Api(tags = ["log"])
@Tag(name = "log", description = "日志模块")
@RestController
@RequestMapping("/log")
open class LogController(private val operationLogService: OperationLogService, private val jwtTokenUtil: JwtTokenUtil) {


    /**
     * 查询操作日志列表
     */
    @GetMapping("/list")
    open fun list(
        @RequestParam(required = false) beginTime: String?,
        @RequestParam(required = false) endTime: String?,
        @RequestParam(required = false) logName: String?,
        @RequestParam(required = false) logType: Int?
    ): Any {
        var page: Page<OperationLog?> = PageFactory<OperationLog>().defaultPage()
        if (StringUtil.isNotEmpty(beginTime)) {
            page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parseDate(beginTime))
        }
        if (StringUtil.isNotEmpty(endTime)) {
            page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parseDate(endTime))
        }
        page.addFilter("logname", SearchFilter.Operator.LIKE, logName)
        if (logType != null) {
            page.addFilter(SearchFilter.build("logtype", SearchFilter.Operator.EQ, BizLogType.valueOf(logType)))
        }
        page = operationLogService.queryPage(page)
        page.setRecords(LogWrapper(BeanUtil.objectsToMaps(page.getRecords())).warp() as List<OperationLog?>)
        return Rets.success(page)
    }

    /**
     * 查询指定用户的操作日志列表
     */
    @GetMapping("/queryByUser")
    open fun list(): Any {
        val page: Page<OperationLog> = Page<OperationLog>()
        page.addFilter(SearchFilter.build("userid", SearchFilter.Operator.EQ, HttpUtil.getUserId(jwtTokenUtil)))
        val pageResult: Page<OperationLog> = operationLogService.queryPage(page)
        return Rets.success(pageResult.records)
    }

    /**
     * 清空日志
     */
    @DeleteMapping
    @BussinessLog(value = "清空业务日志")
    open fun delLog(): Any {
        operationLogService!!.clear()
        return success()
    }
}
