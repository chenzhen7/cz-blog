package com.chenzhen.blog.service.impl;

import com.chenzhen.blog.entity.pojo.SysConfig;
import com.chenzhen.blog.entity.vo.BlogVO;
import com.chenzhen.blog.entity.vo.FeedItemVO;
import com.chenzhen.blog.mapper.BlogMapper;
import com.chenzhen.blog.service.FeedService;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.util.SiteUrlUtil;
import com.chenzhen.blog.util.SyndicationDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenZhen
 * @Description RSS 订阅源实现
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Service
public class FeedServiceImpl implements FeedService {

    /**
     * 订阅源里最多放多少篇。
     * 再多对订阅者没有意义：阅读器只关心「最近有没有更新」，
     * 而订阅端通常按小时轮询，条目越多每次拉取的响应越大。
     */
    private static final int FEED_MAX_ITEMS = 20;

    /**
     * 订阅源标题 = 站长名 + 这个后缀，与站内 <title> 里的写法一致（例：ChenZhen的客栈）
     */
    private static final String FEED_TITLE_SUFFIX = "的客栈";

    /**
     * 订阅源简介：与首页 index.html 的 meta description 是同一句。
     * <p>
     * 不取 t_sys_config.site_profile：那一段是个人简介，多段、还内嵌了 &lt;strong&gt; 标签，
     * 放进 channel 的 description 里会带着标签原样显示。
     */
    private static final String FEED_DESCRIPTION =
            "ChenZhen客栈是一个界面友好，用于记录陈震的个人生活、学习、成长过程，并以文章、图片、音乐、资源等方式进行显示的个人博客网站";

    /**
     * RSS 模板名。写全文件名（含 .xml）：XML 解析器就是靠 *.xml 这个后缀认出该由它渲染的
     * （见 ThymeleafXmlConfig），写成 "feed" 会落到默认的 .html 解析器上。
     */
    private static final String FEED_TEMPLATE = "feed.xml";

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SiteUrlUtil siteUrlUtil;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Override
    public String renderRss() {
        // 复用 getBlogList()：它已经按 published = 1 过滤、按 create_time 倒序，
        // 正好是订阅源要的顺序。本站文章量很小，在内存里取前 20 条即可，不必为此再加一个带 limit 的 SQL
        List<BlogVO> blogs = blogMapper.getBlogList();

        List<FeedItemVO> items = new ArrayList<>();
        for (BlogVO blog : blogs) {
            if (items.size() >= FEED_MAX_ITEMS) {
                break;
            }
            // create_time 这一列在库里是 NULL DEFAULT NULL，但本应用写入的行一定由
            // DateMetaObjectHandler 在 insert 时填上，实际不会为空（当前库里 47 行均为非空）。
            // 所以这里直接格式化：真出现空值是数据不合规，让它当场抛出并留下堆栈，
            // 而不是编一个时间（比如拿当前时间）顶上 —— 那样订阅端会以为文章刚更新过。
            String pubDate = SyndicationDateUtil.toRfc822(blog.getCreateTime());
            items.add(new FeedItemVO(
                    blog.getTitle(),
                    siteUrlUtil.absolute("/blog/" + blog.getId()),
                    blog.getDescription(),
                    pubDate));
        }

        Context context = new Context();
        context.setVariable("title", sysConfigService.list().get(0).getAuthor() + FEED_TITLE_SUFFIX);
        context.setVariable("description", FEED_DESCRIPTION);
        context.setVariable("link", siteUrlUtil.absolute("/"));
        // 订阅源自己的地址，写进 channel 的 atom:link rel="self"
        context.setVariable("feedUrl", siteUrlUtil.absolute("/rss"));
        context.setVariable("items", items);
        // lastBuildDate 取最新一篇的发布时间（列表已按创建时间倒序）。
        // 不用「当前时间」：那样每次拉取都不同，订阅源看起来像一直在更新，阅读器与 CDN 缓存都会被搅乱。
        if (!items.isEmpty()) {
            context.setVariable("lastBuildDate", items.get(0).getPubDate());
        }

        return springTemplateEngine.process(FEED_TEMPLATE, context);
    }
}
