package com.chenzhen.blog.entity.vo;

import com.chenzhen.blog.entity.dto.MessageReplyDTO;
import com.chenzhen.blog.entity.pojo.Message;
import lombok.Data;

import java.util.List;

@Data
public class MessageVO extends Message {

    //该留言的回复列表
    private List<MessageReplyDTO> messageReplyList;
}
