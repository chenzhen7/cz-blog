package com.chenzhen.blog.util;

import com.chenzhen.blog.entity.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/27 11:59
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Component
public class MailUtil {

    @Autowired
    private JavaMailSender javaMailSender;//引入 JavaMailSender 邮件发送对象 来实现发送邮件的功能

    @Autowired
    private SpringTemplateEngine springTemplateEngine;//Spring 模板引擎

    //邮箱模板路径 /resourece/templates下
    @Value("${spring.mail.comment-reminder-template}")
    private String commentReminderTemplate;
    @Value("${spring.mail.friend-link-reminder-template}")
    private String friendLinkReminderTemplate;


    /**
     * 发送 thymeleaf 页面邮件
     */
    @Async("asyncThreadPoolTaskExecutor")  //设置为一个异步方法
    public void sendCommentReminderEmail(Mail mail) throws MessagingException {


        MimeMessage msg = javaMailSender.createMimeMessage();//构建邮件
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);//构建邮件收发信息。


        helper.setFrom(mail.getFrom());//发件人（默认固定为自己）
        helper.setSentDate(mail.getSendDate());//发送日期
        helper.setSubject(mail.getSubject());//发送主题

        Context context = new Context();//将mail中的值设置进context交由模板引擎渲染

        context.setVariable("name",mail.getName());
        context.setVariable("theme", mail.getSubject());
        context.setVariable("comment", mail.getComment());
        context.setVariable("respondent", mail.getRespondent());
        context.setVariable("reply", mail.getReply());
        context.setVariable("address", mail.getAddress());

        String process = springTemplateEngine.process(commentReminderTemplate, context);
        //内容是否设置成html,true代表是
        helper.setText(process,true);
        //收件人
        helper.setTo(mail.getAcceptMailAccount());
        //发送
        javaMailSender.send(msg);

    }

    @Async("asyncThreadPoolTaskExecutor")
    public void sendFriendLinkReminderEmail(Mail mail) throws MessagingException {

        MimeMessage msg = javaMailSender.createMimeMessage();//构建邮件
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);//构建邮件收发信息。


        helper.setFrom(mail.getFrom());//发件人（默认固定为自己）
        helper.setSentDate(mail.getSendDate());//发送日期
        helper.setSubject(mail.getSubject());//发送主题

        Context context = new Context();//将mail中的值设置进context交由模板引擎渲染

        context.setVariable("name",mail.getName());

        String process = springTemplateEngine.process(friendLinkReminderTemplate, context);
        //内容是否设置成html,true代表是
        helper.setText(process,true);
        //收件人
        helper.setTo(mail.getAcceptMailAccount());
        //发送
        javaMailSender.send(msg);
    }
}



