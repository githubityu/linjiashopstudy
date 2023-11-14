package com.ityu.common.dao.system

import com.ityu.common.bean.entity.system.Notice
import com.ityu.common.dao.BaseRepository

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
interface NoticeRepository : BaseRepository<Notice?, Long?> {
    fun findByTitleLike(name: String?): List<Notice?>?
}
