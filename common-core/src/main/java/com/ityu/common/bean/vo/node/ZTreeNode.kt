package com.ityu.common.bean.vo.node

/**
 * jquery ztree 插件的节点
 *
 * @author fengshuonan
 * @date 2017年2月17日 下午8:25:14
 */
open class ZTreeNode {
     open var id: Long? = null //节点id
      open var pId: Long? = null //父节点id
     open var name: String? = null //节点名称
     open var open: Boolean? = null //是否打开节点
     open var checked: Boolean? = null //是否被选中
     open var nodeData: Any? = null //自定义数据

    companion object {
        fun createParent(): ZTreeNode {
            val zTreeNode = ZTreeNode()
            zTreeNode.checked = true
            zTreeNode.id = 0L
            zTreeNode.name = "顶级"
            zTreeNode.open = true
            zTreeNode.pId = (0L)
            return zTreeNode
        }
    }
}
