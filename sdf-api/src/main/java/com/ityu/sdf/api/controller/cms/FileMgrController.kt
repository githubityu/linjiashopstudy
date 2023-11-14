package com.ityu.sdf.api.controller.cms

import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.entity.system.FileInfo
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.bean.vo.query.SearchFilter.Companion.build
import com.ityu.common.service.system.FileService
import com.ityu.common.utils.StringUtil
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@Api(tags = ["fileMgr"])
@Tag(name = "fileMgr", description = "文件")
@RestController
@RequestMapping("/fileMgr")
open class FileMgrController(private val fileService: FileService) {

    @GetMapping(value = ["/list"])
    open fun list(
        @RequestParam(required = false) originalFileName: String?
    ): Any {
        var page = PageFactory<FileInfo>().defaultPage()
        if (StringUtil.isNotEmpty(originalFileName)) {
            page.addFilter(build("originalFileName", SearchFilter.Operator.LIKE, originalFileName))
        }
        page = fileService.queryPage(page)
        return success(page)
    }
}
