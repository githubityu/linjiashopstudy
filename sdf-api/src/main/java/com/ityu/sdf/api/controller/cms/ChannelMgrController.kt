package com.ityu.sdf.api.controller.cms

import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.cms.Channel
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.service.cms.ChannelService
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * 栏目管理
 */
@Api(tags = ["channel"])
@Tag(name = "channel", description = "栏目管理")
@RestController
@RequestMapping("/channel")
open class ChannelMgrController(private val channelService: ChannelService) {

    @PostMapping
    @BussinessLog(value = "编辑栏目", key = "name")
    open fun save(@RequestBody channel: @Valid Channel?): Any {
        if (channel!!.id == null) {
            channelService!!.insert(channel)
        } else {
            channelService!!.update(channel)
        }
        return success()
    }

    @DeleteMapping
    @BussinessLog(value = "删除栏目", key = "id")
    open fun remove(id: Long): Any {
        channelService!!.delete(id)
        return success()
    }

    @GetMapping(value = ["/list"])
    open fun list(): Any {
        val list = channelService!!.queryAll()
        return success(list)
    }
}
