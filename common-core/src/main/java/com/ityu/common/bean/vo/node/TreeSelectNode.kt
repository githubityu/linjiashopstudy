package com.ityu.common.bean.vo.node

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * 配合vue-treeselect使用的节点对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class TreeSelectNode {
    open var id: String? = null
    open var label: String? = null
    open var children: List<TreeSelectNode>? = null
}
