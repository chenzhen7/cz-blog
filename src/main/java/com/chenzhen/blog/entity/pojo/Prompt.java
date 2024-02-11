package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_prompt
 */
@TableName(value ="t_prompt")
@Data
public class Prompt implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String content;

    private String originalContent;

    private String description;

    private Integer views;

    private Integer promptUsageCount;

    private Date createTime;

    private Date updateTime;

    private boolean recommend;

    private boolean published;

    private PromptCategory promptCategory;

    private static final long serialVersionUID = 1L;


}