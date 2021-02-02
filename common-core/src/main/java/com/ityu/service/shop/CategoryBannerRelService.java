package com.ityu.service.shop;



import com.ityu.bean.entity.shop.CategoryBannerRel;
import com.ityu.dao.shop.CategoryBannerRelRepository;
import com.ityu.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryBannerRelService extends BaseService<CategoryBannerRel,Long, CategoryBannerRelRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryBannerRelRepository categoryBannerRelRepository;

}

