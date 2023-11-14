package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.Task
import com.ityu.common.bean.entity.system.TaskLog
import com.ityu.common.bean.vo.front.Ret
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.service.task.TaskLogService
import com.ityu.common.service.task.TaskService
import com.ityu.common.utils.StringUtil
import com.ityu.common.utils.factory.Page
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * Created  on 2018/4/9 0009.
 * 系统参数
 *
 * @author enilu
 */
@Api(tags = ["task"])
@Tag(name = "task", description = "任务模块")
@RestController
@RequestMapping("/task")
open class TaskController(private val taskService: TaskService, private val taskLogService: TaskLogService) {


    /**
     * 获取定时任务管理列表
     */
    @GetMapping(value = ["/list"])
    open fun list(name: String?): Any {
        return if (StringUtil.isNullOrEmpty(name)) {
            success(taskService!!.queryAll())
        } else {
            success(taskService!!.queryAllByNameLike(name))
        }
    }

    /**
     * 新增定时任务管理
     */
    @PostMapping
    @BussinessLog(value = "编辑定时任务", key = "name")
    open fun add(@RequestBody task: @Valid Task?): Any {
        val validRet: Ret<*> = taskService.validate(task)
        if (!validRet.success) {
            return validRet
        }
        if (task?.id == null) {
            taskService.save(task)
        } else {
            val old: Task = taskService.get(task.id)
            old.apply {
                task.let {
                    name = it.name
                    cron = it.cron
                    jobClass = it.jobClass
                    note = it.note
                    data = it.data
                }
            }

            taskService.update(old)
        }
        return Rets.success()
    }

    /**
     * 删除定时任务管理
     */
    @DeleteMapping
    @BussinessLog(value = "删除定时任务", key = "taskId")
    open fun delete(@RequestParam id: Long?): Any {
        taskService.delete(id)
        return Rets.success()
    }

    @PostMapping(value = ["/disable"])
    @BussinessLog(value = "禁用定时任务", key = "taskId")
    open fun disable(@RequestParam taskId: Long?): Any {
        taskService.disable(taskId)
        return Rets.success()
    }

    @PostMapping(value = ["/enable"])
    @BussinessLog(value = "启用定时任务", key = "taskId")
    open fun enable(@RequestParam taskId: Long?): Any {
        taskService.enable(taskId)
        return Rets.success()
    }


    @GetMapping(value = ["/logList"])
    open fun logList(@RequestParam taskId: Long?): Any {
        var page: Page<TaskLog?> = PageFactory<TaskLog>().defaultPage()
        page.addFilter(SearchFilter.build("idTask", SearchFilter.Operator.EQ, taskId))
        page = taskLogService.queryPage(page)
        return Rets.success(page)
    }
}
