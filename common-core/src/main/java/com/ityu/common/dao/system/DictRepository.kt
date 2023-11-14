package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.Dict
import com.ityu.common.dao.BaseRepository

interface DictRepository : BaseRepository<Dict?, Long?> {
    fun findByPid(pid: Long?): List<Dict?>?
    fun findByNameAndPid(name: String?, pid: Long?): List<Dict?>?
    fun findByNameLike(name: String?): List<Dict?>?
}
