
package com.ityu.dao.system;


import com.ityu.bean.entity.system.Cfg;
import com.ityu.dao.BaseRepository;

/**
 * 全局参数dao
 *
 * @author ：enilu
 * @date ：Created in 2019/6/29 12:50
 */
public interface CfgRepository extends BaseRepository<Cfg,Long> {

    Cfg findByCfgName(String cfgName);
}
