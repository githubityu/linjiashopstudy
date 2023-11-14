package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.Task
import com.ityu.common.dao.BaseRepository

interface TaskRepository : BaseRepository<Task?, Long?> {
    fun countByNameLike(name: String?): Long
    fun findByNameLike(name: String?): List<Task?>?
    fun findAllByDisabled(disable: Boolean): List<Task?>?
}
