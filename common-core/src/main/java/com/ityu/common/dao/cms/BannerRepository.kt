package com.ityu.common.dao.cms

import com.ityu.common.bean.entity.cms.Banner
import com.ityu.common.dao.BaseRepository

interface BannerRepository : BaseRepository<Banner?, Long?> {
    /**
     * 查询指定类别的banner列表
     *
     * @param type
     * @return
     */
    fun findAllByType(type: String?): List<Banner?>?
}
