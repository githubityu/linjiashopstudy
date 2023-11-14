package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.LoginLog
import com.ityu.common.dao.BaseRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
interface LoginLogRepository : BaseRepository<LoginLog?, Long?> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from t_sys_login_log")
    fun clear(): Int
}
