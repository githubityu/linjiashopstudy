package com.ityu.service.task;



import com.ityu.bean.entity.system.TaskLog;
import com.ityu.dao.system.TaskLogRepository;
import com.ityu.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志服务类
 * @author  enilu
 * @date 2019-08-13
 */
@Service
public class TaskLogService extends BaseService<TaskLog,Long, TaskLogRepository> {

}
