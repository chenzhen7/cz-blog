package com.chenzhen.blog.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/27 11:52
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
@Accessors(chain = true)
public class Mail {

    //发件人邮箱账号
    private String from;
    //收件人邮箱账号
    private String acceptMailAccount;
    //收件人姓名
    private String name;
    //收件人评论的内容
    private String comment;
    //回复收件人的 回复者的姓名
    private String respondent;
    //回复者的回复内容
    private String reply;
    //评论发生的地方链接（回复者是在哪里回复收件人的）
    private String address;
    //邮件主题
    private String subject;
    //发送时间
    private Date sendDate = new Date();

    public Mail() {
    }

    public Mail(String from, String acceptMailAccount, String name, String comment, String respondent, String reply, String address, String subject) {
        this.from = from;
        this.acceptMailAccount = acceptMailAccount;
        this.name = name;
        this.comment = comment;
        this.respondent = respondent;
        this.reply = reply;
        this.address = address;
        this.subject = subject;
    }



}
