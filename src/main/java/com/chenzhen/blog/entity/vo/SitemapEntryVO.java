package com.chenzhen.blog.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenZhen
 * @Description sitemap.xml 里的一条 URL 记录
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SitemapEntryVO {

    /**
     * 页面绝对地址
     */
    private String loc;

    /**
     * 最后修改时间，W3C 格式，例：2026-09-30T10:20:00+08:00
     * <p>
     * 为 null 只用于静态页（首页、归档、友链等）：那些页面在库里没有对应的行，
     * 也就没有「最后修改时间」这个信息，模板会跳过 {@code <lastmod>} 这个可选元素。
     * 不用当前时间或文章创建时间顶上 —— 那是编造数据，会让搜索引擎以为页面天天在改，反复重抓。
     */
    private String lastmod;
}
