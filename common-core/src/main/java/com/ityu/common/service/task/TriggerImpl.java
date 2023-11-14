package com.ityu.common.service.task;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

import java.time.Instant;
import java.util.Date;

/**
 * @desc: cron触发设置器
 * <p>
 * by  zhang.Z
 */
public class TriggerImpl implements Trigger {

    private String cronExpress;

    public TriggerImpl(String cronExpress) {
        this.cronExpress = cronExpress;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        CronTrigger cronTrigger = new CronTrigger(cronExpress);
        return cronTrigger.nextExecutionTime(triggerContext);
    }

    @Override
    public Instant nextExecution(TriggerContext triggerContext) {
        CronTrigger cronTrigger = new CronTrigger(cronExpress);
        return cronTrigger.nextExecution(triggerContext);
    }
}
