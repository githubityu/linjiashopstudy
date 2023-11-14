package com.ityu.common.service.system;


import com.ityu.common.bean.entity.system.Cfg;
import com.ityu.common.cache.ConfigCache;
import com.ityu.common.dao.system.CfgRepository;
import com.ityu.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CfgService
 *
 * @author enilu
 * @version 2018/11/17 0017
 */

@Service
@Transactional
public class CfgService extends BaseService<Cfg, Long, CfgRepository> {
    @Autowired
    private ConfigCache configCache;

    public Cfg saveOrUpdate(Cfg cfg) {
        if (cfg.getId() == null) {
            insert(cfg);
        } else {
            update(cfg);
        }
        configCache.cache();
        return cfg;
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
        configCache.cache();
    }

}
