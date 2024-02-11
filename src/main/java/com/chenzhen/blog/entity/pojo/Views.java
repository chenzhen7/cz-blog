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
 * @TableName t_views
 */
@TableName(value ="t_views")
@Data
public class Views extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 网站的总浏览量（包含所有文章及页面的浏览量）
     */
    private Long totalViews;

    /**
     * 昨天的总浏览量
     */
    private Long yesterdayViews;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}