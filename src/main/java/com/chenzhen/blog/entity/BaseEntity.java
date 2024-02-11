package com.chenzhen.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/1/11 16:33
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class BaseEntity {

    @TableField(fill = FieldFill.INSERT ,value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE ,value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
