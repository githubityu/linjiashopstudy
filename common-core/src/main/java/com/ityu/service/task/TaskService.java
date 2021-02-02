package com.ityu.service.task;



import com.ityu.bean.entity.system.Task;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.QuartzJob;
import com.ityu.dao.system.TaskRepository;
import com.ityu.service.base.BaseService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务服务类
 * @author  enilu
 * @date 2019-06-13
 */
@Service
public class TaskService extends BaseService<Task,Long, TaskRepository> {
	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private JobService jobService;


	public Task save(Task task) {
		logger.info("新增定时任务%s", task.getName());
		task = taskRepository.save(task);
		try {
			jobService.addJob(jobService.getJob(task));
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		return task;
	}

	@Override
	public Task update(Task record) {
		logger.info("更新定时任务:{}", record.getName());
		taskRepository.save(record);
		try {
			QuartzJob job = jobService.getJob(record.getId().toString(), record.getJobGroup());
			if (job != null) {
				jobService.deleteJob(job);
			}
			jobService.addJob(jobService.getJob(record));
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		return record;
	}


	public Task disable(Long id) {
		Task task = get(id);
		task.setDisabled(true);
		taskRepository.save(task);
		logger.info("禁用定时任务:{}", id.toString());
		try {
			QuartzJob job = jobService.getJob(task.getId().toString(), task.getJobGroup());
			if (job != null) {
				jobService.deleteJob(job);
			}
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		return task;
	}


	public Task enable(Long id) {
		Task task = get(id);
		task.setDisabled(false);
		taskRepository.save(task);
		logger.info("启用定时任务{}", id.toString());
		try {
			QuartzJob job = jobService.getJob(task.getId().toString(), task.getJobGroup());
			if (job != null) {
				jobService.deleteJob(job);
			}
			if (!task.isDisabled()) {
				jobService.addJob(jobService.getJob(task));
			}
		} catch (SchedulerException e) {
			throw  new ApplicationException(BizExceptionEnum.TASK_CONFIG_ERROR);
		}
		return task;
	}



	@Override
	public void deleteById(Long id) {
		Task task = get(id);
		task.setDisabled(true);
		taskRepository.delete(task);
		try {
			logger.info("删除定时任务{}", id.toString());
			QuartzJob job = jobService.getJob(task);
			if (job != null) {
				jobService.deleteJob(job);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}


	public List<Task> queryAllByNameLike(String name) {
		return taskRepository.findByNameLike("%"+name+"%");
	}
}
