package com.ityu.common.dao.cms

import com.ityu.common.bean.entity.cms.Article
import com.ityu.common.dao.BaseRepository

interface ArticleRepository : BaseRepository<Article?, Long?> {
    /**
     * 查询指定栏目下所有文章列表
     *
     * @param idChannel
     * @return
     */
    fun findAllByIdChannel(idChannel: Long?): List<Article?>?
}
