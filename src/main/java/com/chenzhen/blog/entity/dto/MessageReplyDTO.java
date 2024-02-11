package com.chenzhen.blog.entity.dto;

import com.chenzhen.blog.entity.pojo.Message;
import lombok.Data;

@Data
public class MessageReplyDTO extends Message {
    //父评论 昵称
    private String parentNickname;

}
