package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName 标签
 */
@TableName(value ="t_tag")
@Data
public class Tag extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    //标签名
    private String name;

    private static final long serialVersionUID = 1L;


}