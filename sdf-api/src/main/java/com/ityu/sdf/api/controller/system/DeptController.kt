package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.Dept
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.exception.Asserts
import com.ityu.common.exception.ResultCode
import com.ityu.common.service.system.DeptService
import com.ityu.common.service.system.LogObjectHolder
import com.ityu.common.utils.BeanUtil
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * DeptContoller
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@Api(tags = ["dept"])
@Tag(name = "dept", description = "配置")
@RestController
@RequestMapping("/dept")
open class DeptController(private val deptService: DeptService) {
    private val logger = LoggerFactory.getLogger(MenuController::class.java)

    @GetMapping(value = ["/list"])
    open fun list(): Any {
        val list = deptService!!.queryAllNode()
        return success(list)
    }

    @PostMapping
    @BussinessLog(value = "编辑部门", key = "simplename")
    open fun save(@RequestBody dept: @Valid Dept?): Any {
        if (BeanUtil.isOneEmpty(dept, dept?.simplename)) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        if (dept?.id != null) {
            val old: Dept = deptService.get(dept.id)
            LogObjectHolder.me().set(old)
            old.apply {
                dept.let {
                    pid = it.pid
                    simplename = it.simplename
                    fullname = it.fullname
                    num = it.num
                }
            }
            deptService.deptSetPids(old)
            deptService.update(old)
        } else {
            deptService.deptSetPids(dept)
            deptService.insert(dept)
        }
        return Rets.success()
    }

    @DeleteMapping
    @BussinessLog(value = "删除部门", key = "id")
    open fun remove(@RequestParam id: Long?): Any {
        if (id == null) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        if (id!! < 5) {
            return Rets.failure("禁止删除初始部门")
        }
        deptService.deleteDept(id)
        return Rets.success()
    }
}
