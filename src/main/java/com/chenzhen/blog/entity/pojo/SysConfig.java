package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 系统配置表
 * @TableName t_sys_config
 */
@TableName(value ="t_sys_config")
@Data
public class SysConfig extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 参数名
     */
    @NotBlank(message = "参数名不能为空")
    private String name;

    /**
     * 参数值
     */
    private String value;

    /**
     * 描述
     */
    private String description;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}