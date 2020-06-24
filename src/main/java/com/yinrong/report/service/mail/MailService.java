package com.yinrong.report.service.mail;

import com.yinrong.report.service.report.MessageBody;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

public class MailService {
	
    private static final String HOST = "smtp.exmail.qq.com";
    private static final Integer PORT = 465;
    private static final String USERNAME = "rd@nflow.cn";
    private static final String PASSWORD = "Xinliu12345";
    private static final String EMAILFROM = "rd@nflow.cn";
    private static JavaMailSender mailSender = createMailSender();

    private static JavaMailSender createMailSender() {
    	
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("UTF-8");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.timeout", "25000");
        properties.setProperty("mail.smtp.port", PORT+"");
        properties.setProperty("mail.smtp.socketFactory.port", PORT+"");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(properties);
        return sender;
    }

    public static void sendHtmlMail(String from,String to, String subject, String html,
    		String attachmentFile) throws MessagingException, UnsupportedEncodingException {
    	
        String[] toList = to.split(";");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFROM, from);
        messageHelper.setTo(toList);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        if (!StringUtils.isEmpty(attachmentFile)) {
            File file = new File(attachmentFile);
            messageHelper.addAttachment(file.getName(), file);
        }
        mailSender.send(mimeMessage);
    }
    
    public static void sendHtmlMailWithMultiFile(String from,String to, String subject, String html,
                                    String[] attachmentFiles) throws MessagingException, UnsupportedEncodingException {
        
        String[] toList = to.split(";");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFROM, from);
        messageHelper.setTo(toList);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        if (attachmentFiles != null && attachmentFiles.length > 0) {
            for (String file : attachmentFiles) {
                File f = new File(file);
                messageHelper.addAttachment(f.getName(), f);
            }
        }
        mailSender.send(mimeMessage);
    }

    public static void sendHtmlMail(String from, String to, String cc, String subject, String html,
                                    String attachmentFile) throws MessagingException, UnsupportedEncodingException {

        String[] toList = to.split(";");
        String[] ccList = null;
        if (!StringUtils.isEmpty(cc)) {
            ccList = cc.split(";");
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFROM, from);
        messageHelper.setTo(toList);
        if (ArrayUtils.isNotEmpty(ccList)) {
            messageHelper.setCc(ccList);
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        if (!StringUtils.isEmpty(attachmentFile)) {
            File file = new File(attachmentFile);
            messageHelper.addAttachment(file.getName(), file);
        }
        mailSender.send(mimeMessage);
    }
    public static void sendHtmlMail(String from, String to, String subject, String html,
                                    List<MessageBody.Attachment> attachmentList) throws MessagingException, UnsupportedEncodingException {

        String[] toList = to.split(";");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFROM, from);
        messageHelper.setTo(toList);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        if (!CollectionUtils.isEmpty(attachmentList)) {
            for (MessageBody.Attachment attachment:attachmentList){
                messageHelper.addAttachment(attachment.getAttachmentFilename(),  new ByteArrayResource(attachment.getAttachmentFile()));
            }

        }
        mailSender.send(mimeMessage);
    }
}
