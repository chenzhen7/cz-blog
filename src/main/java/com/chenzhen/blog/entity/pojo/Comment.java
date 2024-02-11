package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName t_comment
 */
@TableName(value ="t_comment")
@Data
public class Comment extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    //父评论
    private Long parentCommentId;
    //根评论
    private Long rootCommentId;
    // 博客id
    private Long blogId;
    // 昵称
    private String nickname;
    // 邮箱
    private String email;
    // 评论内容
    private String content;
    // 评论者的博客地址
    private String address;
    // 头像
    private String avatar;
    //是否为管理员评论
    private boolean adminComment;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;



}