package com.yinrong.report.service.report;


import com.yinrong.report.entity.ReportData;
import com.yinrong.report.entity.ReportTarget;
import com.yinrong.report.enums.ReportSendEnum;
import lombok.Data;

import java.util.List;

/**
 * @Classname MessageBody
 * @Description
 * @Date 2020/6/16 8:29 下午
 * @Created by yinrong
 */
@Data
public   class MessageBody {

    ReportTarget reportTarget;
    ReportData reportData;

    List<Attachment> attachmentList;


    @Data
    public static class Attachment{
        private byte[] attachmentFile;
        private String  attachmentFilename;

    }
    public String  getHeadMessage(){
        return  reportTarget.getMessage();
    }
    public  void sendMessage(){
        ReportSendEnum reportSendEnum  = ReportSendEnum.getByType(reportTarget.getTargetType());
        reportSendEnum.send(this);


    }

}
