package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName t_message
 */
@TableName(value ="t_message")
@Data
public class Message extends BaseEntity implements Serializable {


    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentMessageId;

    private Long rootMessageId;

    private String nickname;

    private String email;

    private String content;

    private String avatar;

    private String address;

    private boolean adminMessage;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;




}