package com.ityu.common.service.cms

import com.ityu.common.bean.entity.cms.Contacts
import com.ityu.common.dao.cms.ContactsRepository
import com.ityu.common.service.BaseService
import org.springframework.stereotype.Service

@Service
open class ContactsService : BaseService<Contacts?, Long?, ContactsRepository?>()
