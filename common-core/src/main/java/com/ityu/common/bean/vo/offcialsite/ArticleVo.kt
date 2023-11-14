package com.ityu.common.bean.vo.offcialsite

import java.util.*

open class ArticleVo {
    private var id: String? = null
    private var title: String? = null
    private var content: String? = null
    private var author: Author? = null
    private var createAt: Date? = null
    private val replies: List<Reply>? = null

    constructor()
    constructor(id: String?, title: String?, content: String?, author: Author?, createAt: Date?) {
        this.id = id
        this.title = title
        this.content = content
        this.author = author
        this.createAt = createAt
    }
}
