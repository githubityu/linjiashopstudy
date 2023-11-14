package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.entity.system.Notice
import com.ityu.common.bean.vo.front.Ret
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.service.system.NoticeService
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * UserController
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@Api(tags = ["notice"])
@Tag(name = "notice", description = "通知模块")
@RestController
@RequestMapping("/notice")
open class NoticeController(
    private var noticeService: NoticeService,
) {

    /**
     * 获取通知列表
     */
    @GetMapping(value = ["/list"])
    open fun list(condition: String?): Ret<*> {
        var list: List<Notice?>? = null
        list = if (condition.isNullOrEmpty()) {
            noticeService.queryAll()
        } else {
            noticeService.findByTitleLike(condition)
        }
        return Rets.success(list)
    }


}
