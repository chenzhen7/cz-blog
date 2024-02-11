package com.chenzhen.blog.entity.dto;

import com.chenzhen.blog.entity.pojo.Comment;
import lombok.Data;

@Data
public class CommentReplyDTO extends Comment {
    //父评论 昵称
    private String parentNickname;


}
