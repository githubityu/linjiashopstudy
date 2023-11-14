package com.ityu.common.service.task;


import com.ityu.common.bean.entity.system.TaskLog;
import com.ityu.common.dao.system.TaskLogRepository;
import com.ityu.common.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志服务类
 *
 * @author enilu
 * @date 2019-08-13
 */
@Service
public class TaskLogService extends BaseService<TaskLog, Long, TaskLogRepository> {
}
