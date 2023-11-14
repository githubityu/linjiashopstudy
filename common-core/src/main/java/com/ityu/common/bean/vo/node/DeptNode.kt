package com.ityu.common.bean.vo.node

import com.fasterxml.jackson.annotation.JsonInclude
import com.ityu.common.bean.entity.system.Dept

/**
 * DeptNode
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class DeptNode : Dept() {
     open var children: List<DeptNode>? = null
     open var label: String = ""
}
