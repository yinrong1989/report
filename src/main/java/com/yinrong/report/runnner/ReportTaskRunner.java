package com.yinrong.report.runnner;

import com.yinrong.report.entity.ReportTask;
import com.yinrong.report.repository.report.ReportDataRepository;
import com.yinrong.report.repository.report.ReportTargetRepository;
import com.yinrong.report.repository.report.ReportTaskRepository;
import com.yinrong.report.service.report.ReportTaskExecutor;
import com.yinrong.report.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname ReportTaskRunner
 * @Description
 * @Date 2020/6/16 6:30 下午
 * @Created by yinrong
 */
@Slf4j
@Component
public class ReportTaskRunner  implements ApplicationRunner {
    SchedulerFactory factory;
    private static String JOB_GROUP_NAME = "JOB_GROUP_NAME";

    @Autowired
    ReportTaskRepository reportTaskRepository;
    @Autowired
    ReportDataRepository reportDataRepository;
    @Autowired
    ReportTargetRepository reportTargetRepository;
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        init();

    }
    public void init() throws SchedulerException {

        factory = new StdSchedulerFactory();
        // 这里从数据库中获取任务信息数据
        List<ReportTask> reportTaskList = reportTaskRepository.queryByIsValid("1");
        for (ReportTask reportTask : reportTaskList) {
            try {
                addJob(reportTask);
            } catch (SchedulerException e) {
                log.error("新增策略任务失败：{}", reportTask.getName());
            }
        }


    }
    public void addJob(ReportTask reportTask) throws SchedulerException {

        Scheduler scheduler = factory.getScheduler();
        log.debug(scheduler + ".......................................................................................add");
        // TriggerKey triggerKey = TriggerKey.triggerKey(warningStrategy.getName(), TRIGGER_GROUP_NAME);


        JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(reportTask.getId()+"_"+reportTask.getName(), JOB_GROUP_NAME).build();

        jobDetail.getJobDataMap().put("scheduleJob", reportTask);
        jobDetail.getJobDataMap().put("bean", this);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(reportTask.getCron());

        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(reportTask.getName(), JOB_GROUP_NAME).withSchedule(scheduleBuilder).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("注册任务和触发器失败", e);
        }
        scheduler.start();
    }

    public static class QuartzJobFactory implements Job {
        public final Logger log = Logger.getLogger(this.getClass());

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            ReportTask reportTask = (ReportTask) context.getMergedJobDataMap().get("scheduleJob");
            //后续可以考虑通过反射或者springId获取bean以前method,以达到动态执行方法的目的
            ReportTaskExecutor reportTaskExecutor = SpringContextUtil.getBean(ReportTaskExecutor.class);
            reportTaskExecutor.executeTask(reportTask);
        }
    }

}
