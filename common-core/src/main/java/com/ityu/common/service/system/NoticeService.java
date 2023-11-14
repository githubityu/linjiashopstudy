package com.ityu.common.service.system;


import com.ityu.common.bean.entity.system.Notice;
import com.ityu.common.dao.system.NoticeRepository;
import com.ityu.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * descript
 *
 * @author ：enilu
 * @date ：Created in 2019/6/30 11:14
 */
@Service
public class NoticeService extends BaseService<Notice, Long, NoticeRepository> {
    @Autowired
    private NoticeRepository noticeRepository;

    public List<Notice> findByTitleLike(String title) {
        return noticeRepository.findByTitleLike("%" + title + "%");
    }
}
