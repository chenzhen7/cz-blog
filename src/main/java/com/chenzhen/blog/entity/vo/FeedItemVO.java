package com.chenzhen.blog.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenZhen
 * @Description RSS 订阅源里的一条文章条目
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedItemVO {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章绝对地址
     */
    private String link;

    /**
     * 文章摘要，直接取 t_blog.description
     */
    private String description;

    /**
     * 发布时间，RFC 822 格式，例：Mon, 29 Sep 2026 10:00:00 +0800
     * <p>
     * 在 Service 里格式化好再交给模板，不在模板里格式化：模板里的日期格式化跟随请求 Locale，
     * 中文环境下 EEE / MMM 会渲染成「周一」「9月」，RSS 阅读器不认识，订阅时间会乱。
     */
    private String pubDate;
}
