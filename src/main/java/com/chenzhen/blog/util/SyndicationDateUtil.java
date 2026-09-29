package com.chenzhen.blog.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author ChenZhen
 * @Description RSS 与 sitemap 里日期的格式化。两个文件各有各的日期格式，规则收在这里一处
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public final class SyndicationDateUtil {

    /**
     * RSS 的 pubDate 用 RFC 822 格式。
     * <p>
     * 固定 {@link Locale#US}：EEE 与 MMM 两个字段是按 Locale 渲染的，跟随请求 Locale 的话，
     * 中文环境下会输出「周一」「9月」，RSS 阅读器解析不了，订阅时间会乱。
     * 时区用模式字母 Z，输出 {@code +0800}，这是 RSS 的惯例写法（不是 {@code +08:00}）。
     */
    private static final DateTimeFormatter RFC_822 =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    /**
     * sitemap 的 lastmod 用 W3C 格式，时区用模式字母 XXX，输出 {@code +08:00}。
     */
    private static final DateTimeFormatter W3C_DATETIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US);

    private SyndicationDateUtil() {
    }

    /**
     * 转成 RFC 822，用于 RSS 的 pubDate
     *
     * @param dateTime 文章创建时间
     * @return 例：Mon, 29 Sep 2026 10:00:00 +0800
     */
    public static String toRfc822(LocalDateTime dateTime) {
        return RFC_822.format(withSystemZone(dateTime));
    }

    /**
     * 转成 W3C 日期时间，用于 sitemap 的 lastmod
     *
     * @param dateTime 文章最后修改时间
     * @return 例：2026-09-30T10:20:00+08:00
     */
    public static String toW3cDateTime(LocalDateTime dateTime) {
        return W3C_DATETIME.format(withSystemZone(dateTime));
    }

    /**
     * 补上时区。
     * <p>
     * 实体里的时间字段是 {@link LocalDateTime}，它只表示「墙上时间」，本身没有偏移量，
     * 直接拿带 Z / XXX 的模式去格式化会抛 UnsupportedTemporalTypeException: Unsupported field: OffsetSeconds。
     * <p>
     * 补的是 JVM 默认时区，不是写死的 +08:00：库里的值就是 {@code LocalDateTime.now()} 写进去的
     * （见 DateMetaObjectHandler），用的正是 JVM 默认时区，两边取同一个来源才对得上。
     * 写死的话，换一台时区不同的机器部署，输出就会静默错一整个时区。
     */
    private static ZonedDateTime withSystemZone(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault());
    }
}
