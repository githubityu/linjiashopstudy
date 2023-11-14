package com.ityu.common.service.cms

import com.ityu.common.bean.entity.cms.Article
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.bean.vo.query.SearchFilter.Companion.build
import com.ityu.common.dao.cms.ArticleRepository
import com.ityu.common.enumeration.cms.ChannelEnum
import com.ityu.common.service.BaseService
import com.ityu.common.utils.factory.Page
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class ArticleService : BaseService<Article?, Long?, ArticleRepository?>() {
    @Autowired
    private val articleRepository: ArticleRepository? = null

    /**
     * 查询首页最近5条资讯数据
     *
     * @return
     */
    open  fun queryIndexNews(): List<Article?> {
        val page = query(1, 5, ChannelEnum.NEWS.id)
        return page.records
    }

    open fun query(currentPage: Int, size: Int, idChannel: Long?): Page<Article?> {
        val page: Page<Article?> = Page<Article?>(currentPage, size, "id")
        page.addFilter(build("idChannel", SearchFilter.Operator.EQ, idChannel))
        return queryPage(page)
    }
}
