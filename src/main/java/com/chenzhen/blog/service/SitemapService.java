package com.chenzhen.blog.service;

/**
 * @author ChenZhen
 * @Description 站点地图
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public interface SitemapService {

    /**
     * 渲染 sitemap.xml，内容为静态页 + 全部已发布文章
     *
     * @return 完整的 XML 文本
     */
    String renderSitemap();
}
