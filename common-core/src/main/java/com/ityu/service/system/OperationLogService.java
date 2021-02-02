package com.ityu.service.system;



import com.ityu.bean.entity.system.OperationLog;
import com.ityu.dao.system.OperationLogRepository;
import com.ityu.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author enilu
 */
@Service
public class OperationLogService extends BaseService<OperationLog,Long, OperationLogRepository> {

}
