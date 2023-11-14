package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.Role
import com.ityu.common.dao.BaseRepository
import org.springframework.data.jpa.repository.Query

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
interface RoleRepository : BaseRepository<Role?, Long?> {
    @Query(
        nativeQuery = true,
        value = "SELECT id, pId, NAME, ( CASE WHEN (pId = 0 OR pId IS NULL) THEN 'true' ELSE 'false' END ) OPEN FROM t_sys_role"
    )
    fun roleTreeList(): List<*>?

    @Query(
        nativeQuery = true,
        value = "SELECT r.id AS id, pId AS pId, NAME AS NAME, ( CASE WHEN (pId = 0 OR pId IS NULL) THEN 'true' ELSE 'false' END ) \"open\", ( CASE WHEN (r1.ID = 0 OR r1.ID IS NULL) THEN 'false' ELSE 'true' END ) AS checked FROM t_sys_role r LEFT JOIN ( SELECT ID FROM t_sys_role WHERE ID IN (?1)) r1 ON r.ID = r1.ID ORDER BY pId, num ASC"
    )
    fun roleTreeListByRoleId(ids: Array<Long?>?): List<*>?
    fun findByName(roleName: String?): List<*>?
}
