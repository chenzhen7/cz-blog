package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

/**
 * 
 * @TableName t_while_list
 */
@TableName(value ="t_while_list")
@Data
public class WhileList extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 状态【-1.禁止访问  1.允许访问】
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public interface Status{
        /**
         * 禁止访问
         */
        int FORBIDDEN = -1;
        /**
         * 允许访问
         */
        int ALLOWED = 1;
    }
}