package com.ityu.common.service.system

import com.ityu.common.bean.entity.system.Relation
import com.ityu.common.bean.entity.system.Role
import com.ityu.common.bean.vo.node.Node
import com.ityu.common.bean.vo.node.ZTreeNode
import com.ityu.common.dao.system.RelationRepository
import com.ityu.common.dao.system.RoleRepository
import com.ityu.common.service.BaseService
import com.ityu.common.utils.Convert
import org.springframework.stereotype.Service

/**
 * Created  on 2018/3/25 0025.
 *
 * @author enilu
 */
@Service
open class RoleService(private val roleRepository: RoleRepository, private val relationRepository: RelationRepository) :
    BaseService<Role?, Long?, RoleRepository?>() {

    open fun roleTreeList(): MutableList<ZTreeNode>? {
        val list = roleRepository.roleTreeList()
        return list?.let { getZTreeNodes(it) }
    }

    private fun getZTreeNodes(list: List<Any?>): MutableList<ZTreeNode> {
        val treeNodes: MutableList<ZTreeNode> = ArrayList()
        for (i in list.indices) {
            val arr = list[i] as Array<Any>
            val node = ZTreeNode()
            node.id = arr[0].toString().toLong()
            node.pId = arr[1].toString().toLong()
            node.name = arr[2].toString()
            node.open = arr[3].toString().toBoolean()
            node.checked = arr[4].toString().toBoolean()
            treeNodes.add(node)
        }
        return treeNodes
    }

    open fun roleTreeListByRoleId(ids: Array<Long?>?): MutableList<ZTreeNode>? {
        val list = roleRepository.roleTreeListByRoleId(ids)
        return list?.let { getZTreeNodes(it) }
    }

    open fun setAuthority(roleId: Long?, ids: String?) {
        // 删除该角色所有的权限
        relationRepository.deleteByRoleId(roleId)

        // 添加新的权限
        for (id in Convert.toLongArray(true, Convert.toStrArray(",", ids))) {
            val relation = Relation()
            relation.roleid = roleId
            relation.menuid = id
            relationRepository.save(relation)
        }
    }

    open fun delRoleById(roleId: Long) {
        //删除角色
        roleRepository.deleteById(roleId)

        // 删除该角色所有的权限
        relationRepository.deleteByRoleId(roleId)
    }

    open fun generateRoleTree(list: List<ZTreeNode>): List<Node> {
        val nodes: MutableList<Node> = ArrayList()
        for (role in list) {
            val roleNode = Node()
            roleNode.id = role.id
            roleNode.name = role.name
            roleNode.pid = role.pId
            roleNode.checked = role.checked
            nodes.add(roleNode)
        }
        return nodes
    }

    override operator fun get(id: Long?): Role {
        return roleRepository.getOne(id!!)
    }

    open fun findByName(name: String?): MutableList<Any?> {
        return roleRepository.findByName(name)!! as MutableList<Any?>
    }
}
