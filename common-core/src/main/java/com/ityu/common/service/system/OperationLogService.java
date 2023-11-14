package com.ityu.common.service.system;


import com.ityu.common.bean.entity.system.OperationLog;
import com.ityu.common.dao.system.OperationLogRepository;
import com.ityu.common.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author enilu
 */
@Service
public class OperationLogService extends BaseService<OperationLog, Long, OperationLogRepository> {

}
