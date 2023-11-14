package com.ityu.common.domain

/**
 * Swagger自定义配置
 * Created by macro on 2020/7/16.
 */
open class SwaggerProperties {

    /**
     * API文档生成基础路径
     */
     open var apiBasePackage: String? = null

    /**
     * 是否要启用登录认证
     */
     open var enableSecurity: Boolean = false

    /**
     * 文档标题
     */
     open var title: String? = null

    /**
     * 文档描述
     */
     open var description: String? = null

    /**
     * 文档版本
     */
     open var version: String? = null

    /**
     * 文档联系人姓名
     */
     open var contactName: String? = null

    /**
     * 文档联系人网址
     */
     open var contactUrl: String? = null

    /**
     * 文档联系人邮箱
     */
     open var contactEmail: String? = null
}
