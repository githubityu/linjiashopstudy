package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.Menu
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.node.MenuNode
import com.ityu.common.bean.vo.node.Node
import com.ityu.common.bean.vo.node.TreeSelectNode
import com.ityu.common.bean.vo.node.ZTreeNode
import com.ityu.common.exception.Asserts
import com.ityu.common.exception.ResultCode
import com.ityu.common.service.system.LogObjectHolder
import com.ityu.common.service.system.MenuService
import com.ityu.common.service.system.impl.ConstantFactory
import com.ityu.common.utils.Lists
import com.ityu.common.utils.Maps
import com.ityu.common.utils.StringUtil
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * MenuController
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@Api(tags = ["menu"])
@Tag(name = "menu", description = "菜单模块")
@RestController
@RequestMapping("/menu")
open class MenuController(private val menuService: MenuService) {
    private val logger = LoggerFactory.getLogger(MenuController::class.java)


    @GetMapping(value = ["/list"])
    open fun list(): Any {
        val list = menuService!!.getMenus()
        return success(list)
    }

    @GetMapping(value = ["/tree"])
    open fun tree(): Any {
        val list: List<MenuNode> = menuService.getMenus()
        val treeSelectNodes: MutableList<TreeSelectNode> = Lists.newArrayList()
        for (menuNode in list) {
            val tsn: TreeSelectNode = transfer(menuNode)
            treeSelectNodes.add(tsn)
        }
        return Rets.success(treeSelectNodes)
    }

    fun transfer(node: MenuNode): TreeSelectNode {
        val tsn = TreeSelectNode()
        tsn.id = (node.code)
        tsn.label = (node.name)
        if (node.children != null && node.children?.isNotEmpty() == true) {
            val children: MutableList<TreeSelectNode> = Lists.newArrayList()
            for (child in node.children!!) {
                children.add(transfer(child))
            }
            tsn.children = (children)
        }
        return tsn
    }

    @PostMapping
    @BussinessLog(value = "编辑菜单", key = "name")
    open fun save(@RequestBody menu: @Valid Menu?): Any {
        //判断是否存在该编号
        if (menu?.id == null) {
            val existedMenuName: String = ConstantFactory.me().getMenuNameByCode(menu?.code)
            if (StringUtil.isNotEmpty(existedMenuName)) {
                Asserts.fail(ResultCode.EXISTED_THE_MENU)
            }
        }
        //设置父级菜单编号
        menuService.menuSetPcode(menu)
        if (menu?.id == null) {
            menuService.insert(menu)
        } else {
            menuService.update(menu)
        }
        return Rets.success()
    }

    @DeleteMapping
    @BussinessLog(value = "删除菜单", key = "id")
    open fun remove(@RequestParam id: Long?): Any {
        if (id == null) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        //演示环境不允许删除初始化的菜单
        if (id!!.toInt() <= 80) {
            return Rets.failure("演示环境不允许删除初始菜单")
        }
        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(id))
        menuService.delMenuContainSubMenus(id)
        return Rets.success()
    }

    /**
     * 获取菜单树
     */
    @GetMapping(value = ["/menuTreeListByRoleId"])
    open fun menuTreeListByRoleId(roleId: Int?): Any {
        val menuIds = menuService.getMenuIdsByRoleId(roleId)
        var roleTreeList: MutableList<ZTreeNode?>? = null
        roleTreeList = if (menuIds == null || menuIds.isEmpty()) {
            menuService.menuTreeList(null)
        } else {
            menuService.menuTreeList(menuIds)
        }
        val list: List<Node> = menuService.generateMenuTreeForRole(roleTreeList)

        //element-ui中tree控件中如果选中父节点会默认选中所有子节点，所以这里将所有非叶子节点去掉
        val map: MutableMap<Long, ZTreeNode?>? = Lists.toMap(roleTreeList, "id")
        val group: MutableMap<Long, MutableList<ZTreeNode?>>? = Lists.group(roleTreeList, "pId")

        for ((key, value) in group!!) {
            if (value.size > 1) {
                roleTreeList.remove(map!![key])
            }
        }
        val checkedIds: MutableList<Long> = Lists.newArrayList()
        for (zTreeNode in roleTreeList!!) {
            if (zTreeNode!!.checked != null && zTreeNode.checked!! && zTreeNode.pId!!.toInt() !== 0) {
                checkedIds.add(zTreeNode.id!!)
            }
        }
        return Rets.success(Maps.newHashMap("treeData", list, "checkedIds", checkedIds))
    }

    @GetMapping(value = ["/getMenusByRoleids"])
    open fun getMenusByRoleids(roleIds: List<Long>?): Any {
        val menuIds = menuService.getMenusByRoleids(roleIds)
        return Rets.success(menuIds)
    }


}
