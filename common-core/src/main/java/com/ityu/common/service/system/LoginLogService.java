package com.ityu.common.service.system;


import com.ityu.common.bean.entity.system.LoginLog;
import com.ityu.common.dao.system.LoginLogRepository;
import com.ityu.common.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author enilu
 */
@Service
public class LoginLogService extends BaseService<LoginLog, Long, LoginLogRepository> {

}
