package com.ityu.dao.shop;


import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.dao.BaseRepository;

public interface ShopUserRepository extends BaseRepository<ShopUser,Long> {

    ShopUser findByMobile(String mobile);
    ShopUser findByWechatOpenId(String openId);
}

