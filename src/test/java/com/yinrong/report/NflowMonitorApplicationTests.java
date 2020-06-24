package com.yinrong.report;


import com.yinrong.report.entity.ReportTask;
import com.yinrong.report.repository.report.ReportTaskRepository;
import com.yinrong.report.service.report.ReportTaskExecutor;
import com.yinrong.report.util.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootApplication(scanBasePackages = {"com.yinrong.report.config","com.yinrong.report.service"})
//@SpringBootTest(classes={NflowMonitorApplication.class})
@TestPropertySource({"classpath:application.properties","classpath:application-dev.properties"})
public class NflowMonitorApplicationTests {


    @Autowired
    ReportTaskRepository reportTaskRepository;


    @Test
    public void testReportTask() {
        ReportTask reportTask = reportTaskRepository.findOne(1L);
        ReportTaskExecutor reportTaskExecutor = SpringContextUtil.getBean(ReportTaskExecutor.class);
        reportTaskExecutor.executeTask(reportTask);

    }

}
