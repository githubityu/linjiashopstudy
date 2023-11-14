package com.ityu.common.bean.vo.node

/**
 * Node
 *
 * @author enilu
 * @version 2018/11/24 0024
 */
class Node {
     open var id: Long? = null
     open var pid: Long? = null
     open var name: String? = null
     open var checked: Boolean? = null
     open var children: List<Node> = ArrayList(10)
}
