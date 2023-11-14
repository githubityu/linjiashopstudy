package com.ityu.common.cache;


import cn.hutool.extra.spring.SpringUtil;
import com.ityu.common.service.system.impl.ConstantFactory;

/**
 * @author ：enilu
 * @date ：Created in 2020/4/26 19:07
 */
public abstract class BaseCache implements Cache {
    @Override
    public void cache() {
        SpringUtil.getBean(ConstantFactory.class).cleanLocalCache();
    }
}
