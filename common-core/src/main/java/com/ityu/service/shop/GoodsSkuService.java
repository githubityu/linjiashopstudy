package com.ityu.service.shop;



import com.ityu.bean.entity.shop.GoodsSku;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.dao.shop.GoodsSkuRepository;
import com.ityu.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsSkuService extends BaseService<GoodsSku,Long, GoodsSkuRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsSkuRepository goodsSkuRepository;

    public List<GoodsSku> queryByIdGoods(Long idGoods){
        return queryAll(SearchFilter.build("idGoods",idGoods));
    }

}

