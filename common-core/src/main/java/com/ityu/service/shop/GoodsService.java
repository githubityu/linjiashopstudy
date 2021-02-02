package com.ityu.service.shop;



import com.ityu.bean.entity.shop.Goods;
import com.ityu.bean.entity.shop.GoodsSku;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.bean.vo.shop.GoodsVo;
import com.ityu.dao.shop.GoodsRepository;
import com.ityu.dao.system.FileInfoRepository;
import com.ityu.service.base.BaseService;
import com.ityu.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService extends BaseService<Goods,Long, GoodsRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsSkuService goodsSkuService;
    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Override
    public void deleteById(Long id) {
        Goods goods = get(id);
        goods.setIsDelete(true);
        update(goods);
    }
    public GoodsVo getDetail(Long id){
        Goods goods = get(id);
        List<GoodsSku> skuList = goodsSkuService.queryAll(Lists.newArrayList(
                SearchFilter.build("idGoods",id),
                SearchFilter.build("isDeleted",false)
        ));        GoodsVo vo = new GoodsVo();
        vo.setGoods(goods);
        vo.setSkuList(skuList);
        return vo;
    }

    /**
     * 商品上架或者下架
     * @param id
     * @param isOnSale
     */
    public void changeIsOnSale(Long id, Boolean isOnSale) {
        Goods goods = get(id);
        goods.setIsOnSale(isOnSale);
        update(goods);
    }
}

