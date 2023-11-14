package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.Relation
import com.ityu.common.dao.BaseRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
interface RelationRepository : BaseRepository<Relation?, Long?> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from t_sys_relation where roleid=?1")
    fun deleteByRoleId(roleId: Long?): Int
}
