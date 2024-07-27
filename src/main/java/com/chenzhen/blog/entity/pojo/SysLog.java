package com.chenzhen.blog.entity.pojo;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/27 15:46
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统日志条目类。
 * 此类包含系统中特定日志事件的详细信息。
 */
@Data
@Accessors(chain = true)
@TableName("t_sys_log")
public class SysLog extends BaseEntity {

    /**
     * 日志ID。
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * IP地址。
     */
    private String ip;

    /**
     * 日志内容。
     */
    private String content;

    /**
     * 请求参数。
     */
    private String params;

    /**
     * 操作系统。
     */
    private String os;

    /**
     * 浏览器类型。
     */
    private String browser;

    /**
     * 爬虫类型。
     */
    private String spiderType;

    /**
     * 请求URL。
     */
    private String requestUrl;

    /**
     * 引荐来源。
     */
    private String referer;
}
