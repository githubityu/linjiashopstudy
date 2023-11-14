package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.User
import com.ityu.common.dao.BaseRepository
import org.springframework.data.jpa.repository.Query

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
interface UserRepository : BaseRepository<User?, Long?> {
    fun findByAccount(account: String?): User?
    fun findByAccountAndStatusNot(account: String?, status: Int?): User?

    @Query(value = "select * from t_sys_user a where a.id = 1 for update", nativeQuery = true)
    fun findForUpdate(): User?
    fun findByPhone(phone: String?): User?
}
