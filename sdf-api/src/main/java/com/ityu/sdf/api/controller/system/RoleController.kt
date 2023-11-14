package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.constant.Const
import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.Role
import com.ityu.common.bean.entity.system.User
import com.ityu.common.bean.vo.front.Ret
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.node.Node
import com.ityu.common.bean.vo.node.ZTreeNode
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.exception.Asserts
import com.ityu.common.exception.ResultCode
import com.ityu.common.service.system.LogObjectHolder
import com.ityu.common.service.system.NoticeService
import com.ityu.common.service.system.RoleService
import com.ityu.common.service.system.UserService
import com.ityu.common.service.system.impl.ConstantFactory
import com.ityu.common.utils.*
import com.ityu.common.warpper.RoleWrapper
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * UserController
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@Api(tags = ["role"])
@Tag(name = "role", description = "角色模块")
@RestController
@RequestMapping("/role")
open class RoleController(
    private var roleService: RoleService,
    private var userService: UserService,
) {
    @GetMapping(value = ["/list"])
    open fun list(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) code: String?
    ): Ret<*> {
        var page = PageFactory<Role>().defaultPage()
        page.addFilter("name", name)
        page.addFilter("code", code)
        page = roleService.queryPage(page)
        page.setRecords(RoleWrapper(BeanUtil.objectsToMaps(page.records)).warp() as List<Role>)
        return Rets.success(page)
    }

    @PostMapping
    @BussinessLog(value = "编辑角色", key = "name")
    open fun save(@RequestBody role: @Valid Role?): Any {
        if (role?.id == null) {
            roleService.insert(role)
        } else {
            roleService.update(role)
        }
        return Rets.success()
    }

    @DeleteMapping
    @BussinessLog(value = "删除角色", key = "roleId")
    open fun remove(@RequestParam roleId: Long?): Any {
        if (roleId == null) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        if (roleId!!.toInt() < 4) {
            Asserts.fail("不能删除初始角色")
        }
        val userList: List<User?> =
            userService.queryAll(SearchFilter.build("roleid", SearchFilter.Operator.EQ, roleId.toString()))
        if (userList.isNotEmpty()) {
            return Rets.failure("有用户使用该角色，禁止删除")
        }
        //不能删除超级管理员角色
        if (roleId.toInt() == Const.ADMIN_ROLE_ID) {
            return Rets.failure("禁止删除超级管理员角色")
        }
        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId))
        roleService.delRoleById(roleId)
        return Rets.success()
    }

    @PostMapping(value = ["/savePermisson"])
    @BussinessLog(value = "配置角色权限", key = "roleId")
    open fun setAuthority(roleId: Long?, permissions: String?): Any {
        if (BeanUtil.isOneEmpty(roleId)) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        roleService.setAuthority(roleId, permissions)
        return Rets.success()
    }


    /**
     * 获取角色树
     */
    @GetMapping(value = ["/roleTreeListByIdUser"])
    open fun roleTreeListByIdUser(idUser: Long?): Any {
        val user = userService[idUser]
        val roleIds: String? = user!!.roleid
        var roleTreeList = if (StringUtil.isEmpty(roleIds)) {
            roleService.roleTreeList()
        } else {
            val roleArray: Array<Long?> = Convert.toLongArray(",", roleIds)
            roleService.roleTreeListByRoleId(roleArray)
        }
        val list: List<Node> = roleService.generateRoleTree(roleTreeList!!)
        val checkedIds: MutableList<Long> = Lists.newArrayList()
        for (zTreeNode: ZTreeNode in roleTreeList!!) {
            if (zTreeNode.checked == true) {
                checkedIds.add(zTreeNode.id!!)
            }
        }
        return Rets.success(Maps.newHashMap("treeData", list, "checkedIds", checkedIds))
    }


}
