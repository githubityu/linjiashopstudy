package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.Dept
import com.ityu.common.dao.BaseRepository
import org.springframework.data.jpa.repository.Query

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
interface DeptRepository : BaseRepository<Dept?, Long?> {
    fun findByPidsLike(pid: String?): List<Dept?>?

    @Query(
        nativeQuery = true,
        value = "SELECT id, pid AS pId, simplename AS NAME, ( CASE WHEN (pId = 0 OR pId IS NULL) THEN 'true' ELSE 'false' END ) AS open FROM t_sys_dept"
    )
    fun tree(): List<*>?
    fun findBySimplenameLikeOrFullnameLike(name: String?, name2: String?): List<Dept?>?
}
