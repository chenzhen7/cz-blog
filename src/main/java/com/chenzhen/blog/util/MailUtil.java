package com.chenzhen.blog.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.chenzhen.blog.entity.Mail;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MailUtil {

    @Autowired
    private JavaMailSender javaMailSender;//引入 JavaMailSender 邮件发送对象 来实现发送邮件的功能

    @Autowired
    private SpringTemplateEngine springTemplateEngine;//Spring 模板引擎

    //邮箱模板路径 /resourece/templates下
    @Value("${spring.mail.comment-reminder-template}")
    private String commentReminderTemplate;


    /**
     * 发送 thymeleaf 页面邮件
     */
    @Async("asyncThreadPoolTaskExecutor")  //设置为一个异步方法
    public void sendCommentReminderEmail(Mail mail) throws MessagingException {
        //构建邮件
        MimeMessage msg = javaMailSender.createMimeMessage();
        //构建邮件收发信息。
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        //发件人（默认固定为自己）
        helper.setFrom(mail.getFrom());
        //发送日期
        helper.setSentDate(mail.getSendDate());
        //发送主题
        helper.setSubject(mail.getSubject());
        //将mail中的值设置进context交由模板引擎渲染
        Context context = new Context();


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
    public void sendFriendLinkReminderEmail(Mail mail,String template){
        try {
            //构建邮件
            MimeMessage msg = javaMailSender.createMimeMessage();
            //构建邮件收发信息。
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            //发件人（默认固定为自己）
            helper.setFrom(mail.getFrom());
            //发送日期
            helper.setSentDate(mail.getSendDate());
            //发送主题
            helper.setSubject(mail.getSubject());
            //将mail中的值设置进context交由模板引擎渲染
            Context context = new Context();

            context.setVariable("name",mail.getName());
            if (StrUtil.isNotBlank(mail.getReply())){
                context.setVariable("reply",mail.getReply());
            }
            String process = springTemplateEngine.process(template, context);
            //内容是否设置成html,true代表是
            helper.setText(process,true);
            //收件人
            helper.setTo(mail.getAcceptMailAccount());
            //发送
            javaMailSender.send(msg);
        }catch (MessagingException e){
            log.error("发送邮件失败，mail={}", JSONUtil.toJsonStr(mail));
            log.error("e={}",e.getStackTrace());
        }
    }
}



