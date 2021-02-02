package com.ityu.dao.system;



import com.ityu.bean.entity.system.Notice;
import com.ityu.dao.BaseRepository;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface NoticeRepository extends BaseRepository<Notice,Long> {
    List<Notice> findByTitleLike(String name);
}
