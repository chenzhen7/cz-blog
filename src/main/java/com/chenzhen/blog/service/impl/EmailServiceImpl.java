package com.chenzhen.blog.service.impl;

import com.chenzhen.blog.entity.pojo.Comment;
import com.chenzhen.blog.mapper.BlogMapper;
import com.chenzhen.blog.mapper.CommentMapper;
import com.chenzhen.blog.mapper.MessageMapper;
import com.chenzhen.blog.entity.Mail;
import com.chenzhen.blog.entity.pojo.Message;
import com.chenzhen.blog.entity.pojo.User;
import com.chenzhen.blog.service.EmailService;
import com.chenzhen.blog.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogMapper blogMapper;
    //自己的邮箱地址 从yaml配置文件中获取
    @Value("${spring.mail.username}")
    private String myEmail;

    @Override
    @Async("asyncThreadPoolTaskExecutor")  //设置为一个异步方法
    public void sendMail(User user, Message message) throws MessagingException {

        //如果评论是管理员发布且非根评论，则该评论为管理员的回复，则给[回复的对象]发一封提醒邮件
        if (user != null && message.getParentMessageId() != null){
            //获取父评论
            Message parentMessage = messageMapper.selectById(message.getParentMessageId());
            //如果父评论是管理员，不发邮件
            if (parentMessage.isAdminMessage()){
                return;
            }
            Mail mail = new Mail(null, parentMessage.getEmail(), parentMessage.getNickname(), parentMessage.getContent(),
                    message.getNickname(), message.getContent(),
                    "/message", "您在《ChenZhen的客栈-留言板》中的评论有了新的回复！");

            mailUtil.sendThymeleafEmail(mail);

        }else {
            //如果不是管理员发的评论
            if (message.getParentMessageId() == null){
                //如果是根评论
                //发给我自己，提醒有人在留言板留言了
                Mail mail = new Mail(null, myEmail, "ChenZhen", null,
                        message.getNickname(), message.getContent(),
                        "/message","在《ChenZhen的客栈-留言板》中有了新的留言！");

                mailUtil.sendThymeleafEmail(mail);

            }else{
                //如果不是根评论
                //给回复者[回复的对象]发一份提醒邮件
                Message parentMessage = messageMapper.selectById(message.getParentMessageId());
                Mail mail = new Mail(null,parentMessage.getEmail(),parentMessage.getNickname(),
                        parentMessage.getContent(),message.getNickname(),message.getContent(),
                        "/message","您在《ChenZhen的客栈-留言板》中的评论有了新的回复！");

                mailUtil.sendThymeleafEmail(mail);

            }

        }
    }

    @Override
    @Async("asyncThreadPoolTaskExecutor")  //设置为一个异步方法
    public void sendMail(User user, Comment comment) throws MessagingException {
        //评论所在的博文标题
        String title = blogMapper.getTitleById(comment.getBlogId());
        //如果评论是管理员发布且非根评论，则该评论为管理员的回复，则给[回复的对象]发一封提醒邮件
        if (user != null && comment.getParentCommentId() != null){
            //获取父评论
            Comment parentComment = commentMapper.selectById(comment.getParentCommentId() );
            //如果父评论是管理员，不发邮件
            if (parentComment.isAdminComment()){
                return;
            }
            //如果不是根评论，则给[我回复的对象]发一封提醒邮件

            Mail mail = new Mail(null, parentComment.getEmail(), parentComment.getNickname(), parentComment.getContent(),
                    comment.getNickname(), comment.getContent(),
                    "/blog/" + comment.getBlogId(), "您在ChenZhen的博客《" + title + "》中的评论有了新的回复！");
            mailUtil.sendThymeleafEmail(mail);

        }else {
            //如果不是管理员发的评论
            if (comment.getParentCommentId() == null){
                //如果是根评论
                //发给我自己，提醒有人在留言板留言了
                Mail mail = new Mail(null, myEmail, "ChenZhen", null,
                        comment.getNickname(), comment.getContent(),
                        "/blog/" + comment.getBlogId(),"您在ChenZhen的博客《" + title + "》中有了新的评论！");
                mailUtil.sendThymeleafEmail(mail);

            }else{
                //如果不是根评论
                //给回复者[回复的对象]发一份提醒邮件
                Comment parentComment = commentMapper.selectById(comment.getParentCommentId());
                Mail mail = new Mail(null,parentComment.getEmail(),parentComment.getNickname(),
                        parentComment.getContent(),comment.getNickname(),comment.getContent(),
                        "/blog/" + comment.getBlogId(),"您在ChenZhen的博客《" + title + "》中的评论有了新的回复！");
                mailUtil.sendThymeleafEmail(mail);

            }

        }
    }
}
