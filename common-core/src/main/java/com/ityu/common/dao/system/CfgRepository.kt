package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.Cfg
import com.ityu.common.dao.BaseRepository

/**
 * 全局参数dao
 *
 * @author ：enilu
 * @date ：Created in 2019/6/29 12:50
 */
interface CfgRepository : BaseRepository<Cfg?, Long?> {
    fun findByCfgName(cfgName: String?): Cfg?
}
