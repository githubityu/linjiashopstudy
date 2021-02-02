
package com.ityu.dao.system;




import com.ityu.bean.entity.system.Task;
import com.ityu.dao.BaseRepository;

import java.util.List;

public interface TaskRepository extends BaseRepository<Task,Long> {

    long countByNameLike(String name);

    List<Task> findByNameLike(String name);
    List<Task> findAllByDisabled(boolean disable);
}
