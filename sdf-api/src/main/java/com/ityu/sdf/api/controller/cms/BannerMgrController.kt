package com.ityu.sdf.api.controller.cms

import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.cms.Banner
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.bean.vo.query.SearchFilter.Companion.build
import com.ityu.common.service.cms.BannerService
import com.ityu.common.utils.StringUtil
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

/**
 * banner管理
 */
//@Api(tags = ["banner"])
@Tag(name = "banner", description = "banner管理")
@RestController
@RequestMapping("/banner")
open class BannerMgrController(private val bannerService: BannerService) {

    @PostMapping
    @BussinessLog(value = "编辑banner", key = "title")
    open fun save(@RequestBody banner: @Valid Banner?): Any {
        if (banner!!.id == null) {
            bannerService.insert(banner)
        } else {
            bannerService.update(banner)
        }
        return Rets.success()
    }


    @DeleteMapping
    @BussinessLog(value = "删除banner", key = "id")
    open fun remove(id: Long?): Any {
        bannerService.delete(id)
        return success()
    }

    @GetMapping(value = ["/list"])
    open fun list(@RequestParam(required = false) title: String?): Any {
        var filter: SearchFilter? = null
        if (StringUtil.isNotEmpty(title)) {
            filter = build("title", SearchFilter.Operator.LIKE, title)
        }
        val list = bannerService.queryAll(filter)
        return success(list)
    }

}
