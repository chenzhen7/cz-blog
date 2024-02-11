package com.chenzhen.blog.entity;

import com.chenzhen.blog.entity.pojo.Message;
import com.chenzhen.blog.entity.pojo.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 一个自定义的消息类，用来封装 User 对象和 该对象发送的消息（）。
 */

@Data
public class EmailMessage implements Serializable {

    private User user;
    private Message message;

    public EmailMessage() {
    }

    public EmailMessage(User user, Message message) {
        this.user = user;
        this.message = message;
    }


}
