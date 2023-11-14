package com.ityu.common.bean.vo.offcialsite

open class BannerItem {
    open var url = "javascript:"
    open var img: String? = null
    open var title = ""

    constructor()
    constructor(url: String, img: String?, title: String) {
        this.url = url
        this.img = img
        this.title = title
    }
}
