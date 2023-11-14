package com.ityu.common.bean.vo.node

import com.ityu.common.utils.Lists

/**
 * @author ：enilu
 * @date ：Created in 2019/10/30 22:00
 */
class RouterMenu {
     open var id: Long? = null
     open var parentId: Long? = null
     open var path: String? = null
     open var component: String? = null
     open var name: String? = null
     open var num: Int? = null
     open var hidden = false
     open var meta: MenuMeta? = null
     open var children = Lists.newArrayList<RouterMenu>()
}
