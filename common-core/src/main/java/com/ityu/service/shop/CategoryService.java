package com.ityu.service.shop;



import com.ityu.bean.entity.shop.Category;
import com.ityu.dao.shop.CategoryRepository;
import com.ityu.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseService<Category,Long, CategoryRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryRepository categoryRepository;

}

