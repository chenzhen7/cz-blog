package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.Comment;
import com.chenzhen.blog.entity.pojo.Message;
import com.chenzhen.blog.entity.pojo.User;

import javax.mail.MessagingException;

public interface EmailService {

    /**
     * 新增邮件回复功能，有回复消息会有邮件提醒
     * @param user  传入用户参数，判断是否为管理员
     * @param message  留言
     */
    void sendMail(User user, Message message) throws MessagingException;

    /**
     * 新增邮件回复功能，有回复消息会有邮件提醒
     * @param user  传入用户参数，判断是否为管理员
     * @param comment  评论
     */
    void sendMail(User user, Comment comment) throws MessagingException;

}
