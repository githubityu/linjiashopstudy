package com.ityu.common.bean.vo.offcialsite

open class Product {
    open var id: Long? = null
    open var name: String? = null
    open var img: String? = null

    constructor()
    constructor(id: Long?, name: String?, img: String?) {
        this.id = id
        this.name = name
        this.img = img
    }
}
