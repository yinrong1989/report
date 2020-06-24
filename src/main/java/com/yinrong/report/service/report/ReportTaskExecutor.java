package com.yinrong.report.service.report;


import com.yinrong.report.entity.ReportData;
import com.yinrong.report.entity.ReportTarget;
import com.yinrong.report.entity.ReportTask;
import com.yinrong.report.enums.EntityManagerEnum;
import com.yinrong.report.enums.FileType;
import com.yinrong.report.repository.report.ReportDataRepository;
import com.yinrong.report.repository.report.ReportTargetRepository;
import com.yinrong.report.repository.report.ReportTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname ReportTaskExecutor
 * @Description
 * @Date 2020/6/17 5:34 下午
 * @Created by yinrong
 */
@Slf4j
@Component
public class ReportTaskExecutor {
    @Autowired
    ReportTaskRepository reportTaskRepository;
    @Autowired
    ReportDataRepository reportDataRepository;
    @Autowired
    ReportTargetRepository reportTargetRepository;
    public void executeTask(ReportTask reportTask) {
        log.info("任务名称为：{}", reportTask.getName());
        List<Map> mapList = null;
        List<ReportData> reportDataList = reportDataRepository.queryByTaskIdAndIsValid(reportTask.getId(),"1");
        List<MessageBody.Attachment> attachmentList = new ArrayList<>();
        //组装附件
        reportDataList.forEach(reportData -> {
            attachmentList.add(buildAttachment(reportData));
        });
        List<ReportTarget> reportTargetList = reportTargetRepository.queryByTaskIdAndIsValid(reportTask.getId(),"1");
        //组装消息体
        //   List<MessageBody> messageBodyList = new ArrayList<>();
        reportTargetList.forEach(reportTarget -> {
            MessageBody messageBody = new MessageBody();
            messageBody.setReportTarget(reportTarget);
            messageBody.setAttachmentList(attachmentList);
            messageBody.sendMessage();
        });

    }

    public MessageBody.Attachment buildAttachment(ReportData reportData){
        List<Map> mapList = null;
        try {
            EntityManager entityManager  = EntityManagerEnum.getEnum(reportData.getDataSource()).getEntityManager();
            Query query = entityManager
                    .createNativeQuery(reportData.getSqlContent());
//            query.unwrap(SQLQuery.class).setResultTransformer(
//                    //AliasToEntityMapResultTransformer.INSTANCE
//                    Transformers.ALIAS_TO_ENTITY_MAP
//            );
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            mapList = query.getResultList();

//            entityManager
//                    .createNativeQuery(reportData.getSqlContent()).getResultList();
        } catch (Exception e) {
            log.error("sql执行异常：{},异常：{}",reportData.getSqlContent(),e);
            return null;
        }
        MessageBody.Attachment attachment = new MessageBody.Attachment();
        FileType fileType =FileType.getFileType(reportData.getFileType());
        /**1.组装文件名*/
        attachment.setAttachmentFilename(fileType.buildFileName(reportData.getFileName()));
        /**2.将数据转化为文件流*/
        attachment.setAttachmentFile(fileType.createFile(mapList,reportData.getFileName()));

        return attachment;
    }

}
