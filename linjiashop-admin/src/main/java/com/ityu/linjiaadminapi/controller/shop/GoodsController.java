package com.ityu.linjiaadminapi.controller.shop;

import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.core.BussinessLog;

import com.ityu.bean.entity.shop.Goods;
import com.ityu.bean.entity.shop.GoodsSku;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.enumeration.Permission;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.service.shop.GoodsService;
import com.ityu.service.shop.GoodsSkuService;
import com.ityu.service.system.LogObjectHolder;
import com.ityu.utils.Lists;
import com.ityu.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/shop/goods")
public class GoodsController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsSkuService goodsSkuService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(@RequestParam(value = "name", required = false) String name) {
        Page<Goods> page = new PageFactory<Goods>().defaultPage();
        page.addFilter("name", SearchFilter.Operator.LIKE, name);
        page = goodsService.queryPage(page);
        return Rets.success(page);
    }

    @RequestMapping(value = "/saveBaseInfo", method = RequestMethod.POST)
    @BussinessLog(value = "保存商品基本信息", key = "name")
    //@RequiresPermissions(value = {Permission.GOODS_EDIT})
    public Object saveBaseInfo(@RequestBody Goods goods) {
        if (goods.getId() == null) {
            goodsService.insert(goods);
        }
        return Rets.success(goods.getId());
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑商品", key = "name")
    //@RequiresPermissions(value = {Permission.GOODS_EDIT})
    public Object save(@RequestBody @Valid Goods goods) {
        List<SearchFilter> filters = Lists.newArrayList(
                SearchFilter.build("idGoods",goods.getId()),
                SearchFilter.build("isDeleted",false)
        );
        List<GoodsSku> skuList = goodsSkuService.queryAll(filters);        if (goods.getPrice() == null) {
            if (!skuList.isEmpty()) {
                int stock = 0;
                for (GoodsSku sku : skuList) {
                    stock += sku.getStock();
                }
                goods.setStock(stock);
                goods.setPrice(skuList.get(0).getPrice());
            } else {
                throw new ApplicationException(BizExceptionEnum.DATA_ERROR);
            }
        } else {
            if (!skuList.isEmpty()) {
                //如果配置了price，说明是单规格商品，则将之前配置的sku库存皆设置为0;这里最好不要删除sku记录，避免之前下过的订单无法正确关联
                for (GoodsSku sku : skuList) {
                    sku.setStock(0);
                }
                goodsSkuService.update(skuList);
            }
        }
        if (goods.getId() == null) {
            goodsService.insert(goods);
        } else {
            Goods old = goodsService.get(goods.getId());
            LogObjectHolder.me().set(old);
            goods.setCreateBy(old.getCreateBy());
            goods.setCreateTime(old.getCreateTime());
            goodsService.update(goods);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除商品", key = "id")
    //@RequiresPermissions(value = {Permission.GOODS_EDIT})
    public Object remove(Long id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        goodsService.deleteById(id);
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Object get(Long id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        return Rets.success(goodsService.getDetail(id));
    }

    @RequestMapping(value = "/changeIsOnSale", method = RequestMethod.POST)
    //@RequiresPermissions(value = {Permission.GOODS_EDIT})
    public Object changeIsOnSale(@RequestParam("id") Long id, @RequestParam("isOnSale") Boolean isOnSale) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        goodsService.changeIsOnSale(id, isOnSale);
        return Rets.success(goodsService.get(id));
    }

}
