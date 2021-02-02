package com.ityu.service.shop;


import com.ityu.bean.entity.shop.ExpressInfo;
import com.ityu.dao.shop.ExpressInfoRepository;
import com.ityu.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpressInfoService extends BaseService<ExpressInfo,Long, ExpressInfoRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ExpressInfoRepository expressInfoRepository;


}

