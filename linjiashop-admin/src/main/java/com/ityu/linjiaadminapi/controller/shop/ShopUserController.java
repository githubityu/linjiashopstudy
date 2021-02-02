package com.ityu.linjiaadminapi.controller.shop;


import com.ityu.bean.constant.factory.PageFactory;

import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.core.BussinessLog;
import com.ityu.service.shop.CartService;
import com.ityu.service.shop.OrderService;
import com.ityu.service.shop.ShopUserService;
import com.ityu.utils.DateUtil;
import com.ityu.utils.Maps;
import com.ityu.utils.StringUtil;
import com.ityu.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shop/user")
public class ShopUserController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) String mobile, @RequestParam(required = false) String nickName,
                       @RequestParam(required = false) String startRegDate,
                       @RequestParam(required = false) String endRegDate,
                       @RequestParam(required = false) String startLastLoginTime,
                       @RequestParam(required = false) String endLastLoginTime) {
        Page<ShopUser> page = new PageFactory<ShopUser>().defaultPage();
        page.addFilter("mobile", mobile);
        page.addFilter("nickName", nickName);
        if (StringUtil.isNotEmpty(startRegDate)) {
            page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parseTime(startRegDate + " 00:00:00"));
        }
        if (StringUtil.isNotEmpty(endRegDate)) {
            page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parseTime(endRegDate + " 23:59:59"));
        }
        if (StringUtil.isNotEmpty(startLastLoginTime)) {
            page.addFilter("lastLoginTime", SearchFilter.Operator.GTE, DateUtil.parseTime(startLastLoginTime + " 00:00:00"));
        }
        if (StringUtil.isNotEmpty(endLastLoginTime)) {
            page.addFilter("lastLoginTime", SearchFilter.Operator.LTE, DateUtil.parseTime(endLastLoginTime + " 23:59:59"));
        }
        page = shopUserService.queryPage(page);
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑用户", key = "name")
    public Object save(@ModelAttribute ShopUser tShopUser) {
        if (tShopUser.getId() == null) {
            shopUserService.insert(tShopUser);
        } else {
            shopUserService.update(tShopUser);
        }
        return Rets.success();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Object get(@PathVariable("id") Long id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        return Rets.success(shopUserService.get(id));
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public Object getInfo(@PathVariable("id") Long id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        ShopUser shopUser = shopUserService.get(id);

        SearchFilter filter = SearchFilter.build("idUser", id);
        Long cartCount = cartService.count(filter);
        Long orderCount = orderService.count(filter);
        Map<String, Object> data = Maps.newHashMap(
                "cartCount", cartCount,
                "orderCount", orderCount,
                "info", shopUser
        );
        return Rets.success(data);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除用户", key = "id")
    public Object remove(Long id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        shopUserService.deleteById(id);
        return Rets.success();
    }
}
