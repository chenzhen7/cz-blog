package com.chenzhen.blog.service;

/**
 * @author ChenZhen
 * @Description RSS 订阅源
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public interface FeedService {

    /**
     * 渲染 RSS 2.0 订阅源，内容为最新的若干篇已发布文章
     *
     * @return 完整的 XML 文本
     */
    String renderRss();
}
