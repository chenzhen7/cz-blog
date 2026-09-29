package com.chenzhen.blog.config;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Collections;

/**
 * @author ChenZhen
 * @Description 给 XML 模板（feed.xml / sitemap.xml）单独注册一个模板解析器
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Configuration
public class ThymeleafXmlConfig {

    /**
     * 本解析器只接这种名字的模板，用通配符而不是枚举具体文件名，
     * 以后新增 XML 模板不用回来改这里。
     */
    private static final String XML_TEMPLATE_PATTERN = "*.xml";

    /**
     * XML 模板解析器，与默认的 .html 解析器并存（两个都会被 SpringTemplateEngine 收进去，按 order 排）。
     * <p>
     * 为什么要单独一个：默认解析器是「后缀 .html + HTML 模式」，两件事都办不了。
     * <ul>
     *     <li>HTML 模式下 {@code <link>} 按空元素处理，序列化出来是 {@code <link ...>}，没有闭合标签。
     *         这在 XML 里是致命错误，RSS 阅读器解析到一半就报错；而用浏览器打开看却「差不多是对的」，
     *         只有拿真阅读器订阅才看得出来。</li>
     *     <li>HTML 模式还会按 HTML 的规矩处理标签、吃掉 XML 声明。</li>
     * </ul>
     * <p>
     * <b>限定名字范围这件事不是可选优化，是必须的。</b>解析器一旦被选中就不会再问下一个解析器：
     * 实测不设 {@link #XML_TEMPLATE_PATTERN} 时，页面 view name「index」也会落进这个解析器，
     * 它去找 index.xml 找不到，直接抛异常，于是整站每个页面都变成 500
     * （报错是 {@code FileNotFoundException: index.xml}，看起来像模板被删了，其实是解析器抢了活）。
     * 反过来，本解析器接不到的 view name 会返回空、正常落到默认解析器上。
     * <p>
     * 同理 order 也必须显式指定：默认解析器的 order 也是默认值，两边相同的话谁先匹配是不确定的，
     * 不确定就会出现「有时 XML 生效、有时 HTML 生效」这种最难查的问题。
     * <p>
     * 前缀、编码、缓存开关一律复用 {@link ThymeleafProperties}，不在代码里另抄一份字面量 ——
     * 以后改 {@code spring.thymeleaf.*}，这里跟着变，不会两边不一致。
     */
    @Bean
    public SpringResourceTemplateResolver xmlTemplateResolver(ThymeleafProperties properties,
                                                              ApplicationContext applicationContext) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(properties.getPrefix());
        // 后缀留空：调用方传的是带扩展名的完整文件名（如 feed.xml），
        // 传 "feed" 再让后缀补成 .xml 也行，但那样上面那条通配符规则就要跟着改，不划算
        resolver.setCharacterEncoding(properties.getEncoding().name());
        resolver.setTemplateMode(TemplateMode.XML);
        resolver.setCacheable(properties.isCache());
        resolver.setResolvablePatterns(Collections.singleton(XML_TEMPLATE_PATTERN));
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return resolver;
    }
}
