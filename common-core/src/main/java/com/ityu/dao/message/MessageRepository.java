package com.ityu.dao.message;





import com.ityu.bean.entity.message.Message;
import com.ityu.dao.BaseRepository;

import java.util.ArrayList;


public interface MessageRepository extends BaseRepository<Message,Long> {
    void deleteAllByIdIn(ArrayList<String> list);
}

