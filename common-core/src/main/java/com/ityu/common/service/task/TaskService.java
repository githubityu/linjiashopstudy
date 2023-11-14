package com.ityu.common.service.task;


import com.ityu.common.bean.entity.system.Task;
import com.ityu.common.bean.vo.front.Ret;
import com.ityu.common.bean.vo.front.Rets;
import com.ityu.common.dao.system.TaskRepository;
import com.ityu.common.service.BaseService;
import com.ityu.common.service.system.CfgService;
import com.ityu.common.service.task.job.HelloJob;
import com.ityu.common.utils.JsonUtil;
import com.ityu.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务服务类
 *
 * @author enilu
 * @date 2019-06-13
 */
@Service
public class TaskService extends BaseService<Task, Long, TaskRepository> {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private CfgService cfgService;
    @Autowired
    private  TaskLogService taskLogService;


    private static Map<Long, ScheduledFuture<?>> sm = new HashMap<>();


    /**
     * 校验定时人物配置是否合法
     *
     * @param task
     * @return
     */
    public Ret validate(Task task) {
        if (StringUtil.isNotEmpty(task.getData())) {
            // 参数比粗为json格式
            if (!JsonUtil.isJson(task.getData())) {
                return Rets.INSTANCE.failure("参数不是合法的json字符串", null);
            }
        }
        try {
            Class.forName(task.getJobClass());
        } catch (ClassNotFoundException e) {
            return Rets.INSTANCE.failure("执行类配置错误", null);
        }
        boolean cronExpression = CronExpression.isValidExpression(task.getCron());
        if (!cronExpression) {
            return Rets.INSTANCE.failure("定时规则不合法", null);
        }
        return Rets.INSTANCE.success();
    }

    public Task save(Task task) {
        logger.info("新增定时任务{}", task.getName());
        validate(task);
        task = taskRepository.save(task);
        //开启定时任务
        addTask(task);
        return task;
    }

    public void removeTask(long taskId) {
        ScheduledFuture<?> future = sm.get(taskId);
        //判断任务是否为空或者已经结束
        if (null != future && !future.isCancelled()) {
            future.cancel(true);
            while (!future.isDone()) {
                future.cancel(true);
            }
        }
        sm.remove(taskId);
    }

    public void addTask(Task task) {
        try {
            ScheduledFuture<?> schedule = taskScheduler.schedule(new HelloJob(cfgService,taskLogService,task), new TriggerImpl(task.getCron()));
            sm.put(task.getId(), schedule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task update(Task record) {
        logger.info("更新定时任务{}", record.getName());
        validate(record);
        taskRepository.save(record);
        removeTask(record.getId());
        addTask(record);
        return record;
    }


    public Task disable(Long id) {
        Task task = get(id);
        task.setDisabled(true);
        taskRepository.save(task);
        removeTask(id);
        logger.info("禁用定时任务{}", id);
        return task;
    }


    public Task enable(Long id) {
        Task task = get(id);
        task.setDisabled(false);
        taskRepository.save(task);
        addTask(task);
        logger.info("启用定时任务{}", id.toString());
        return task;
    }

    @Override
    public void delete(Long id) {
        Task task = get(id);
        task.setDisabled(true);
        taskRepository.delete(task);
        removeTask(id);
        try {
            logger.info("删除定时任务{}", id.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    public List<Task> queryAllByNameLike(String name) {
        return taskRepository.findByNameLike("%" + name + "%");
    }


}
