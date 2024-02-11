package com.chenzhen.blog.entity.query;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/12/20 22:09
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class BaseQuery implements java.io.Serializable {

    /**
     * 当前页数
     */
    private Integer pageNum;
    /**
     * 每页显示条数
     */
    private Integer pageSize;

    /**
     * 创建开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;
    /**
     * 创建结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;


}