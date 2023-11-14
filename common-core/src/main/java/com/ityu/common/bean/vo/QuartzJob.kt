package com.ityu.common.bean.vo

import java.io.Serializable
import java.util.*

open class QuartzJob : Serializable {
   open var jobId: String? = null
   open var jobName: String? = null
   open var jobGroup: String? = null
   open var jobClass: String? = null
   open var description: String? = null
   open var cronExpression: String? = null
   open var concurrent = false
   open var jobStatus: String? = null
   open var nextTime: Date? = null
   open var previousTime: Date? = null
   open var disabled = false
   open var dataMap: Map<String, Any>? = null
}
