package com.ityu.service.cms;



import com.ityu.bean.entity.cms.Contacts;
import com.ityu.dao.cms.ContactsRepository;
import com.ityu.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ContactsService extends BaseService<Contacts,Long, ContactsRepository> {
}
