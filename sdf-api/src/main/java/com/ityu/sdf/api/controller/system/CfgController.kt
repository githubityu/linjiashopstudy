package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.Cfg
import com.ityu.common.bean.entity.system.FileInfo
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.bean.vo.query.SearchFilter.Companion.build
import com.ityu.common.exception.Asserts
import com.ityu.common.exception.ResultCode
import com.ityu.common.service.system.CfgService
import com.ityu.common.service.system.FileService
import com.ityu.common.service.system.LogObjectHolder
import com.ityu.common.utils.Maps
import com.ityu.common.utils.StringUtil
import com.ityu.common.utils.factory.Page
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * CfgController
 *
 * @author enilu
 * @version 2018/11/17 0017
 */
@Api(tags = ["cfg"])
@Tag(name = "cfg", description = "配置")
@RestController
@RequestMapping("/cfg")
open class CfgController(private val cfgService: CfgService, private val fileService: FileService) {
    private val logger = LoggerFactory.getLogger(javaClass)


    /**
     * 查询参数列表
     */
    @GetMapping(value = ["/list"])
    open fun list(
        @RequestParam(required = false) cfgName: String?,
        @RequestParam(required = false) cfgValue: String?
    ): Any {
        var page = PageFactory<Cfg>().defaultPage()
        if (StringUtil.isNotEmpty(cfgName)) {
            page.addFilter(build("cfgName", SearchFilter.Operator.LIKE, cfgName))
        }
        if (StringUtil.isNotEmpty(cfgValue)) {
            page.addFilter(build("cfgValue", SearchFilter.Operator.LIKE, cfgValue))
        }
        page = cfgService!!.queryPage(page)
        return success(page)
    }

    /**
     * 导出参数列表
     *
     * @param cfgName
     * @param cfgValue
     * @return
     */
    @GetMapping(value = ["/export"])
    open fun export(
        @RequestParam(required = false) cfgName: String?,
        @RequestParam(required = false) cfgValue: String?
    ): Any {
        var page: Page<Cfg> = PageFactory<Cfg>().defaultPage()
        if (StringUtil.isNotEmpty(cfgName)) {
            page.addFilter(build("cfgName", SearchFilter.Operator.LIKE, cfgName))
        }
        if (StringUtil.isNotEmpty(cfgValue)) {
            page.addFilter(build("cfgValue", SearchFilter.Operator.LIKE, cfgValue))
        }
        page = cfgService.queryPage(page)
        val fileInfo: FileInfo = fileService.createExcel(
            "templates/config.xlsx",
            "系统参数.xlsx",
            Maps.newHashMap("list", page.records) as Map<String, Any>?
        )
        return Rets.success(fileInfo)
    }

    @PostMapping
    @BussinessLog(value = "新增参数", key = "cfgName")
    open fun add(@RequestBody cfg: @Valid Cfg?): Any {
        cfgService.saveOrUpdate(cfg)
        return Rets.success()
    }

    @PutMapping
    @BussinessLog(value = "编辑参数", key = "cfgName")
    open fun update(@RequestBody cfg: @Valid Cfg?): Any {
        val old: Cfg = cfgService.get(cfg?.id)
        LogObjectHolder.me().set(old)
        old.apply {
            cfg?.let {
                cfgName = it.cfgName
                cfgValue = it.cfgValue
                cfgDesc = it.cfgDesc
            }
        }
        cfgService.saveOrUpdate(old)
        return Rets.success()
    }

    @DeleteMapping
    @BussinessLog(value = "删除参数", key = "id")
    open fun remove(@RequestParam id: Long?): Any {
        logger.info("id:{}", id)
        if (id == null) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        if (id!! < 9) {
            return Rets.failure("禁止删除初始化参数")
        }
        cfgService.delete(id)
        return Rets.success()
    }
}
