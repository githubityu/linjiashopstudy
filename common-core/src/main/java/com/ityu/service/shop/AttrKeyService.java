package com.ityu.service.shop;



import com.ityu.bean.constant.cache.Cache;
import com.ityu.bean.entity.shop.AttrKey;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.dao.shop.AttrKeyRepository;
import com.ityu.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttrKeyService extends BaseService<AttrKey,Long, AttrKeyRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AttrKeyRepository attrKeyRepository;
    @CacheEvict(value = Cache.APPLICATION, key = "#root.targetClass.simpleName+':'+#idCategory")
    public List<AttrKey> queryBy(Long idCategory ){
        return queryAll(SearchFilter.build("idCategory",idCategory));
    }

}

