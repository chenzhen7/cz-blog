package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 
 * @TableName t_friend
 */
@TableName(value ="t_friend")
@Data
public class Friend extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    // 博客地址
    @NotEmpty(message = "博客地址不能为空")
    private String blogAddress;

    // 博客名称
    @NotEmpty(message = "博客名称不能为空")
    private String blogName;

    // 博客图片
    @NotEmpty(message = "博客图片不能为空")
    private String pictureAddress;

    // 博客介绍
    private String blogDescription;

    // 审核状态【0.审核中 1.通过 -1.不通过】
    private Integer status;

    // 拒绝理由
    private String reason;

    //通知邮箱
    private String email;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public interface Status{
        //审核中
        int AUDITING = 0;
        //通过
        int PASS = 1;
        //拒绝
        int REJECT = -1;
    }

}