package com.ityu.service.system;


import com.ityu.bean.entity.system.User;
import com.ityu.cache.CacheDao;
import com.ityu.dao.system.UserRepository;
import com.ityu.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/23 0023.
 *
 * @author enilu
 */
@Service
public class ManagerService extends BaseService<User, Long, UserRepository> {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CacheDao cacheDao;

    public User findByAccount(String account) {
        //由于：@Cacheable标注的方法，如果其所在的类实现了某一个接口，那么该方法也必须出现在接口里面，否则cache无效。
        //具体的原因是， Spring把实现类装载成为Bean的时候，会用代理包装一下，所以从Spring Bean的角度看，只有接口里面的方法是可见的，其它的都隐藏了，自然课看不到实现类里面的非接口方法，@Cacheable不起作用。
        //所以这里手动控制缓存
        User user = cacheDao.hget(CacheDao.SESSION, account, User.class);
        if (user != null) {
            return user;
        }
        user = userRepository.findByAccount(account);
        cacheDao.hset(CacheDao.SESSION, account, user);
        return user;
    }

    @Override
    public User update(User record) {
        cacheDao.hdel(CacheDao.SESSION, record.getAccount());
        userRepository.save(record);
        return record;
    }

}
