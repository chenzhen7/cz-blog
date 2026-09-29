package com.chenzhen.blog.controller;

import com.chenzhen.blog.service.FeedService;
import com.chenzhen.blog.service.SitemapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ChenZhen
 * @Description 订阅源与站点地图。两个接口都是只读的，Controller 只做响应封装，内容由 Service 渲染
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
public class FeedController {

    @Autowired
    private FeedService feedService;
    @Autowired
    private SitemapService sitemapService;

    /**
     * RSS 2.0 订阅源
     * <p>
     * 不用 controller 返回视图名的方式，直接把渲染好的 XML 当响应体写出去：
     * 走视图解析器的话，响应头会被 ThymeleafViewResolver 统一改成 text/html，
     * 而 RSS 阅读器会据此拒绝解析（拿不到 XML 类型就不认这个订阅源）。
     * <p>
     * 不加 @BlogLog：这两个接口是给爬虫和阅读器轮询的，通常按小时访问一次，
     * 记进 t_sys_log 只会把日志表灌满，没有排查价值。
     */
    @GetMapping(value = "/rss", produces = "application/rss+xml;charset=UTF-8")
    @ResponseBody
    public String rss() {
        return feedService.renderRss();
    }

    /**
     * 站点地图
     */
    @GetMapping(value = "/sitemap.xml", produces = "application/xml;charset=UTF-8")
    @ResponseBody
    public String sitemap() {
        return sitemapService.renderSitemap();
    }
}
