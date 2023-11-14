package com.ityu.common.service.task.job;


import com.ityu.common.bean.entity.system.Cfg;
import com.ityu.common.bean.entity.system.Task;
import com.ityu.common.bean.entity.system.TaskLog;
import com.ityu.common.exception.Asserts;
import com.ityu.common.service.system.CfgService;
import com.ityu.common.service.task.TaskLogService;
import com.ityu.common.utils.DateUtil;
import com.ityu.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * HelloJob
 *
 * @author zt
 * @version 2018/12/30 0030
 */
public class HelloJob implements Runnable {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final CfgService cfgService;
    private final TaskLogService taskLogService;
    private final Task task;

    public HelloJob(CfgService cfgService, TaskLogService taskLogService, Task task) {
        this.cfgService = cfgService;
        this.taskLogService = taskLogService;
        this.task = task;
    }

    @Override
    public void run() {
        String exeResult = "执行成功";
        final TaskLog taskLog = new TaskLog();
        taskLog.setName(task.getName());
        final Date exeAt = new Date();
        taskLog.setExecAt(exeAt);
        taskLog.setIdTask(task.getId());
        //默认是成功 出异常后改成失败
        taskLog.setExecSuccess(TaskLog.EXE_SUCCESS_RESULT);
        try {
            Cfg cfg = cfgService.queryAll().stream().findFirst().get();
            cfg.setCfgDesc("update by " + DateUtil.getTime());
            cfgService.update(cfg);
            Asserts.fail("失败了");
        } catch (Exception e) {
            exeResult = "执行失败\n";
            exeResult += StringUtil.getStackTrace(e);
            taskLog.setExecSuccess(TaskLog.EXE_FAILURE_RESULT);
            taskLog.setJobException(e.getClass().getName());
        }
        task.setExecResult(exeResult);
        task.setExecAt(exeAt);
        logger.info("hello :HelloJob");
        taskLogService.insert(taskLog);

    }
}



