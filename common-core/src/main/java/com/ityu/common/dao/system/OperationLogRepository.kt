package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.OperationLog
import com.ityu.common.dao.BaseRepository
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query


//import javax.transaction.Transactional

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
interface OperationLogRepository : BaseRepository<OperationLog?, Long?> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from t_sys_operation_log")
    fun clear(): Int
}
