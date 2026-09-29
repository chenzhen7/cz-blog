package com.chenzhen.blog.service.impl;

import com.chenzhen.blog.entity.vo.BlogVO;
import com.chenzhen.blog.entity.vo.SitemapEntryVO;
import com.chenzhen.blog.mapper.BlogMapper;
import com.chenzhen.blog.service.SitemapService;
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
 * @Description 站点地图实现
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Service
public class SitemapServiceImpl implements SitemapService {

    /**
     * 静态页路径。
     * <p>
     * 这是站点的路由表，不是数据，所以写在代码里而不是查库。全站只此一处列静态页 ——
     * 新增或删除页面时必须同步改这里，否则新页面进不了 sitemap（漏了不会有任何报错，
     * 只会表现为「搜索引擎一直不收录」）。
     * <p>
     * 顺序与顶部导航一致，方便对着导航检查有没有漏。
     */
    private static final String[] STATIC_PATHS = {
            "/",         // 首页
            "/tags",     // 标签
            "/archives", // 归档
            "/message",  // 留言板
            "/friends",  // 友链
            "/about"     // 关于我
    };

    /**
     * sitemap 模板名。写全文件名（含 .xml）：XML 解析器就是靠 *.xml 这个后缀认出该由它渲染的
     * （见 ThymeleafXmlConfig），写成 "sitemap" 会落到默认的 .html 解析器上。
     */
    private static final String SITEMAP_TEMPLATE = "sitemap.xml";

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private SiteUrlUtil siteUrlUtil;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Override
    public String renderSitemap() {
        List<SitemapEntryVO> entries = new ArrayList<>();

        // 静态页没有「最后修改时间」这个数据，lastmod 传 null，由模板决定不输出该元素。
        // 不拿当前时间顶上：每次都变，会让搜索引擎以为页面天天在改，反复重抓。
        for (String path : STATIC_PATHS) {
            entries.add(new SitemapEntryVO(siteUrlUtil.absolute(path), null));
        }

        // 与 RSS 共用同一个查询（已发布、按创建时间倒序）。sitemap 本身对顺序没有要求，
        // 用同一个来源是为了保证两个文件收录的文章集合永远一致，不会一边有、一边没有
        for (BlogVO blog : blogMapper.getBlogList()) {
            // update_time 由 DateMetaObjectHandler 在 insert 与 update 时维护，实际不会为空
            // （当前库里 47 行均为非空）。真出现空值是数据不合规，让它当场抛出并留下堆栈
            entries.add(new SitemapEntryVO(
                    siteUrlUtil.absolute("/blog/" + blog.getId()),
                    SyndicationDateUtil.toW3cDateTime(blog.getUpdateTime())));
        }

        Context context = new Context();
        context.setVariable("entries", entries);
        return springTemplateEngine.process(SITEMAP_TEMPLATE, context);
    }
}
