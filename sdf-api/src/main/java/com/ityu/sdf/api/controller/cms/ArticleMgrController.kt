package com.ityu.sdf.api.controller.cms

import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.cms.Article
import com.ityu.common.bean.vo.front.Ret
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.service.cms.ArticleService
import com.ityu.common.utils.DateUtil
import com.ityu.common.utils.JsonUtil
import com.ityu.sdf.api.utils.ApiConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.*

/**
 * 文章管理
 */
//@Api(tags = ["article"])
@Tag(name = "article", description = "文章管理")
@RestController
@RequestMapping("/article")
open class ArticleMgrController(private val articleService: ArticleService) {

    @PostMapping
    @BussinessLog(value = "编辑文章", key = "name")
    open fun save(): Ret<*> {
        val article = JsonUtil.getFromJson(Article::class.java)
        if (article.id != null) {
            val old = articleService[article.id]
            old!!.author = article.author
            old.content = article.content
            old.idChannel = article.idChannel
            old.img = article.img
            old.title = article.title
            articleService.update(old)
        } else {
            articleService!!.insert(article)
        }
        return success()
    }


    @DeleteMapping
    @BussinessLog(value = "删除文章", key = "id")
    open fun remove(id: Long?): Any {
        articleService.delete(id)
        return success()
    }

    @GetMapping
    open fun get(@Param("id") id: Long?): Any {
        val article = articleService[id]
        return success(article)
    }

    @GetMapping(value = ["/list"])
    open fun list(
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) author: String?,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): Any {
        var page = PageFactory<Article?>().defaultPage()
        page.addFilter("title", SearchFilter.Operator.LIKE, title)
        page.addFilter("author", SearchFilter.Operator.EQ, author)
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"))
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"))
        page = articleService!!.queryPage(page)
        return success(page)
    }
}
