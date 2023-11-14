package com.ityu.common.bean.vo.node

import java.util.*

/**
 * @author fengshuonan
 * @Description 菜单的节点
 * @date 2016年12月6日 上午11:34:17
 */
class MenuNode : Comparable<Any?> {
    /**
     * 节点id
     */
     open var id: Long? = null

    /**
     * 父节点
     */
     open var parentId: Long? = null

    /**
     * 父菜单编码
     */
     open var pcode: String? = null

    /**
     * 节点名称
     */
     open var name: String? = null

    /**
     * 按钮级别
     */
     open var levels: Int? = null

    /**
     * 按钮级别
     */
     open var ismenu: Int? = null
     open var isMenuName: String? = null
        get() = if (ismenu == 1) "是" else "否"

    /**
     * 按钮的排序
     */
     open var num: Int? = null

    /**
     * 节点的url
     */
     open var url: String? = null
     open var path: String? = null

    /**
     * 节点图标
     */
     open var icon: String? = null

    /**
     * 菜单编码
     */
     open var code: String? = null

    /**
     * 組件配置
     */
     open var component: String? = null

    /**
     * 子节点的集合
     */
     open var children: List<MenuNode>? = ArrayList(10)

    /**
     * 查询子节点时候的临时集合
     */
    open var linkedList: MutableList<MenuNode> = ArrayList()
     open var hidden: Boolean? = null

    constructor() : super()
    constructor(id: Long?, parentId: Long?) : super() {
        this.id = id
        this.parentId = parentId
    }

    override fun toString(): String {
        return "MenuNode{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", levels=" + levels +
                ", num=" + num +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                ", linkedList=" + linkedList +
                '}'
    }

    override fun compareTo(o: Any?): Int {
        val menuNode = o as MenuNode?
          var num = menuNode!!.num
        if (num == null) {
            num = 0
        }
        return this.num!!.compareTo(num)
    }

    /**
     * 构建整个菜单树
     *
     * @author fengshuonan
     */
    fun buildNodeTree(nodeList: List<MenuNode>) {
        for (treeNode in nodeList) {
            val linkedList = treeNode.findChildNodes(nodeList, treeNode.id)
            if (linkedList!!.size > 0) {
                treeNode.children = linkedList
            }
        }
    }

    /**
     * 查询子节点的集合
     *
     * @author fengshuonan
     */
    fun findChildNodes(nodeList: List<MenuNode>?, parentId: Long?): List<MenuNode>? {
        if (nodeList == null && parentId == null) {
            return null
        }
        val iterator = nodeList!!.iterator()
        while (iterator.hasNext()) {
            val node = iterator.next()
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (node.parentId != 0L && parentId == node.parentId) {
                recursionFn(nodeList, node, parentId)
            }
        }
        return linkedList
    }

    /**
     * 遍历一个节点的子节点
     *
     * @author fengshuonan
     */
    fun recursionFn(nodeList: List<MenuNode>?, node: MenuNode, pId: Long?) {
        val childList = getChildList(nodeList, node) // 得到子节点列表
        if (childList.size > 0) { // 判断是否有子节点
            if (node.parentId == pId) {
                linkedList.add(node)
            }
            val it = childList.iterator()
            while (it.hasNext()) {
                recursionFn(nodeList, it.next(), pId)
            }
        } else {
            if (node.parentId == pId) {
                linkedList.add(node)
            }
        }
    }

    /**
     * 得到子节点列表
     *
     * @author fengshuonan
     */
    private fun getChildList(list: List<MenuNode>?, node: MenuNode): List<MenuNode> {
        val nodeList: MutableList<MenuNode> = ArrayList()
        val it = list!!.iterator()
        while (it.hasNext()) {
            val n = it.next()
            if (n.parentId == node.id) {
                nodeList.add(n)
            }
        }
        return nodeList
    }

    companion object {
        fun createRoot(): MenuNode {
            return MenuNode(0L, -1L)
        }

        /**
         * 清除掉按钮级别的资源
         *
         * @date 2017年2月19日 下午11:04:11
         */
        fun clearBtn(nodes: List<MenuNode>): List<MenuNode> {
            val noBtns = ArrayList<MenuNode>()
            for (node in nodes) {
                if (node.ismenu == IsMenu.YES.code) {
                    noBtns.add(node)
                }
            }
            return noBtns
        }

        /**
         * 清除所有二级菜单
         *
         * @date 2017年2月19日 下午11:18:19
         */
        fun clearLevelTwo(nodes: List<MenuNode>): List<MenuNode> {
            val results = ArrayList<MenuNode>()
            for (node in nodes) {
                val levels = node.levels
                if (levels == 1) {
                    results.add(node)
                }
            }
            return results
        }

        /**
         * 构建菜单列表
         *
         * @date 2017年2月19日 下午11:18:19
         */
        fun buildTitle(nodes: List<MenuNode>): List<MenuNode> {
            val clearBtn = clearBtn(nodes)
            MenuNode().buildNodeTree(clearBtn)
            val menuNodes = clearLevelTwo(clearBtn)

            //对菜单排序
            Collections.sort(menuNodes)

            //对菜单的子菜单进行排序
            for (menuNode in menuNodes) {
                if (menuNode.children != null && menuNode.children!!.size > 0) {
                    Collections.sort(menuNode.children)
                }
            }
            return menuNodes
        }
    }
}
