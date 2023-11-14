package com.ityu.common.bean.vo.offcialsite

import java.util.*

open class News {
    /**
     * 资讯标题
     */
    open var desc: String? = null

    /**
     * 详情链接
     */
    open var url: String? = null

    /**
     * 图片地址
     */
    open var src: String? = null
    open val id: Long? = null
    open val createTime: Date? = null

    constructor()
    constructor(desc: String?, url: String?, src: String?) {
        this.desc = desc
        this.url = url
        this.src = src
    }
}
