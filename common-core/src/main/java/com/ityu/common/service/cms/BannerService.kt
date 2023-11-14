package com.ityu.common.service.cms

import com.ityu.common.bean.entity.cms.Banner
import com.ityu.common.bean.vo.offcialsite.BannerVo
import com.ityu.common.dao.cms.BannerRepository
import com.ityu.common.enumeration.cms.BannerTypeEnum
import com.ityu.common.service.BaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class BannerService : BaseService<Banner?, Long?, BannerRepository?>() {
    @Autowired
    private val bannerRepository: BannerRepository? = null

    /**
     * 查询首页banner数据
     *
     * @return
     */
    open fun queryIndexBanner(): BannerVo {
        return queryBanner(BannerTypeEnum.INDEX.value)
    }

    open fun queryBanner(type: String?): BannerVo {
        val banner = BannerVo()
        val bannerList = bannerRepository!!.findAllByType(type)
        banner.index = 0
        banner.list = bannerList
        return banner
    }
}
