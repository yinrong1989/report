package com.yinrong.report.enums;

import com.yinrong.report.entity.ReportTarget;
import com.yinrong.report.service.mail.MailService;
import com.yinrong.report.service.report.MessageBody;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @Classname ReportSendEnum
 * @Description
 * @Date 2020/6/17 11:08 上午
 * @Created by yinrong
 */
public enum  ReportSendEnum {


    WeChat("weChat"){
        @Override
        public void send(MessageBody messageBody) {

        }
    },
    Mail("mail"){
        @Override
        public void send(MessageBody messageBody) {
            try {
                ReportTarget reportTarget = messageBody.getReportTarget();
                MailService.sendHtmlMail("NFSP-资金平台",reportTarget.getTargetAddress() ,
                        DateFormatUtils.format(new Date(),"yyyy-MM-dd")+reportTarget.getMessage(), "Dear All:\n" +
                                "附件是报表文件",
                        messageBody.getAttachmentList());
            } catch (MessagingException e) {
                logger.error(e.toString());
            } catch (UnsupportedEncodingException e) {
                logger.error(e.toString());
            }
        }
    };

    private static Logger logger = LoggerFactory.getLogger(ReportSendEnum.class);
    public String code;

    ReportSendEnum(String code) {
        this.code = code;
    }
    public abstract  void send(MessageBody messageBody);
    public static ReportSendEnum  getByType(String type){
        for (ReportSendEnum reportSendEnum:ReportSendEnum.values()){
            if (reportSendEnum.name().equalsIgnoreCase(type)){
                return reportSendEnum;
            }
        }

        return  ReportSendEnum.Mail;
    }
}
