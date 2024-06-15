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
 * @author chenzhen
 * @TableName t_type
 */
@TableName(value ="t_type")
@Data
public class Type extends BaseEntity implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}