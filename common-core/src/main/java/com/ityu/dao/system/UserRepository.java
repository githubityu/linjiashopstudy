package com.ityu.dao.system;



import com.ityu.bean.entity.system.User;
import com.ityu.dao.BaseRepository;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface UserRepository extends BaseRepository<User, Long> {
    User findByAccount(String account);
}
