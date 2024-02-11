package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName t_prompt_category
 */
@TableName(value ="t_prompt_category")
@Data
@AllArgsConstructor
public class PromptCategory implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private static final long serialVersionUID = 1L;




}