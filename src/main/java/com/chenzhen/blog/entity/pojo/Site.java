package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_site
 */
@TableName(value ="t_site")
@Data
public class Site implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String thumb;

    private String description;

    private String url;

    private Integer categoryId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;


}