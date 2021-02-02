package com.ityu.service.system;




import com.ityu.bean.entity.system.LoginLog;
import com.ityu.dao.system.LoginLogRepository;
import com.ityu.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author enilu
 */
@Service
public class LoginLogService extends BaseService<LoginLog,Long, LoginLogRepository> {

}
