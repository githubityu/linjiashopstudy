package com.ityu.common.cache;


import com.ityu.common.bean.entity.system.Dict;

import java.util.List;

/**
 * 字典缓存
 *
 * @author zt
 * @version 2018/12/23 0023
 */
public interface DictCache extends Cache {

    List<Dict> getDictsByPname(String dictName);

    String getDict(Long dictId);
}
