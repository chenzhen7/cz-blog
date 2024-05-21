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

    //自己的邮箱地址 从yaml配置文件中获取
    @Value("${spring.mail.username}")
    private String from;
    //邮箱模板路径 /resourece/templates下
    @Value("${spring.mail.thymeleaf-html}")
    private String html;


    /**
     * 发送 thymeleaf 页面邮件
     */
    @Async("asyncThreadPoolTaskExecutor")  //设置为一个异步方法
    public void sendThymeleafEmail(Mail mail) throws MessagingException {


        MimeMessage msg = javaMailSender.createMimeMessage();//构建邮件
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);//构建邮件收发信息。


            helper.setFrom(from);//发件人（默认固定为自己）
            helper.setSentDate(mail.getSendTime());//发送日期
            helper.setSubject(mail.getTheme());//发送主题


            Context context = new Context();//将mail中的值设置进context交由模板引擎渲染
//            WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());

            context.setVariable("name",mail.getName());
            context.setVariable("theme", mail.getTheme());
            context.setVariable("comment", mail.getComment());
            context.setVariable("respondent", mail.getRespondent());
            context.setVariable("reply", mail.getReply());
            context.setVariable("address", mail.getAddress());

            String process = springTemplateEngine.process(html, context);
            helper.setText(process,true);//内容是否设置成html,true代表是
            helper.setTo(mail.getAcceptMailAccount());//收件人
            javaMailSender.send(msg);//发送

        }
    }



