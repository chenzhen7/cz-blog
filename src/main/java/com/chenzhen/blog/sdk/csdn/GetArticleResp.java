package com.chenzhen.blog.sdk.csdn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author chenjixian
 * @date 2024/7/18 10:20
 * @description:
 */
@Data
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetArticleResp implements Serializable {

    /**
     * 文章ID
     */
    private String article_id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章描述
     */
    private String description;

    /**
     * 文章内容
     */
    private String content;

    /**
     * markdown内容
     */
    private String markdown_content;

    /**
     * 文章标签
     */
    private String tags;

    /**
     * 文章分类
     */
    private String categories;

    /**
     * 文章类型 (如：original表示原创)
     */
    private String type;

    /**
     * 文章状态 (2表示什么状态，具体业务定义)
     */
    private int status;

    /**
     * 阅读类型 (如：public表示公开)
     */
    private String read_type;

    /**
     * 原因 (例如，审核未通过原因)
     */
    private String reason;

    /**
     * 资源URL
     */
    private String resource_url;

    /**
     * 资源ID
     */
    private String resource_id;

    /**
     * 原始链接
     */
    private String original_link;

    /**
     * 授权状态
     */
    private boolean authorized_status;

    /**
     * 是否检查原创
     */
    private boolean check_original;

    /**
     * 编辑器类型 (如：1表示某种编辑器类型)
     */
    private int editor_type;

    /**
     * 计划 (例如，文章的计划任务)
     */
    private List<String> plan;

    /**
     * 投票ID
     */
    private int vote_id;

    /**
     * 计划发布时间 (0表示没有计划)
     */
    private long scheduled_time;

    /**
     * 文章级别
     */
    private String level;
}
