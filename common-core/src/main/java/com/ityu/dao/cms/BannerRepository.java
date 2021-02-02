
package com.ityu.dao.cms;



import com.ityu.bean.entity.cms.Banner;
import com.ityu.dao.BaseRepository;

import java.util.List;

public interface BannerRepository extends BaseRepository<Banner,Long> {
    /**
     * 查询指定类别的banner列表
     * @param type
     * @return
     */
    List<Banner> findAllByType(String type);
}
