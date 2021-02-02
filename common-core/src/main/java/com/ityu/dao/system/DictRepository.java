
package com.ityu.dao.system;



import com.ityu.bean.entity.system.Dict;
import com.ityu.dao.BaseRepository;

import java.util.List;

public interface DictRepository extends BaseRepository<Dict,Long> {
    List<Dict> findByPid(Long pid);
    List<Dict> findByNameAndPid(String name, Long pid);

    List<Dict> findByNameLike(String name);
}
