package com.ityu.common.domain

/**
 * Controller层的日志封装类
 * Created by macro on 2018/4/26.
 */
open class WebLog {
    /**
     * 操作描述
     */
    val description: String? = null

    /**
     * 操作用户
     */
    val username: String? = null

    /**
     * 操作时间
     */
    val startTime: Long? = null

    /**
     * 消耗时间
     */
    val spendTime: Int? = null

    /**
     * 根路径
     */
    val basePath: String? = null

    /**
     * URI
     */
    val uri: String? = null

    /**
     * URL
     */
    val url: String? = null

    /**
     * 请求类型
     */
    val method: String? = null

    /**
     * IP地址
     */
    val ip: String? = null

    /**
     * 请求参数
     */
    val parameter: Any? = null

    /**
     * 返回结果
     */
    val result: Any? = null
}
