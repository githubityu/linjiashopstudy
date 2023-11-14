package com.ityu.common.service.cms

import com.ityu.common.bean.entity.cms.Channel
import com.ityu.common.dao.cms.ChannelRepository
import com.ityu.common.service.BaseService
import org.springframework.stereotype.Service

/**
 * 文章频道service
 *
 * @author ：enilu
 * @date ：Created in 2019/6/30 13:06
 */
@Service
open class ChannelService : BaseService<Channel?, Long?, ChannelRepository?>()
