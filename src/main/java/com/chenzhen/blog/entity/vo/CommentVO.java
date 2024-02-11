package com.chenzhen.blog.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.chenzhen.blog.entity.dto.CommentReplyDTO;
import com.chenzhen.blog.entity.pojo.Comment;
import lombok.Data;

import java.util.List;

@Data
public class CommentVO extends Comment {

    //该评论的回复列表
    private List<CommentReplyDTO> commentReplyList;

}
