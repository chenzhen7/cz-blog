package com.chenzhen.blog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ChenZhen
 * @Description 站点对外访问地址的工具，用于把站内路径拼成绝对地址（RSS 与 sitemap 需要）
 * @create 2026/9/30 10:20
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Component
public class SiteUrlUtil {

    /**
     * 站点对外访问地址，结尾不带斜杠，例：https://www.chenzhen.space
     * <p>
     * 只从配置取，不从 HttpServletRequest 推：生产环境跑在反向代理后面，
     * 请求头里的 Host 可能是内网地址或 IP，拿它拼出来的链接会被写进订阅源，
     * 订阅者点开就是坏的。地址属于部署信息，本来就该由配置提供。
     */
    private final String siteUrl;

    public SiteUrlUtil(@Value("${blog.site-url}") String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * 把站内路径拼成绝对地址
     *
     * @param path 站内路径，必须以 / 开头，例：/blog/48
     * @return 绝对地址，例：https://www.chenzhen.space/blog/48
     */
    public String absolute(String path) {
        // 少了开头的斜杠会拼出 https://www.chenzhen.spaceblog/48 这种坏链接，
        // 而且不会有任何报错，所以这里直接拦下来，不做补斜杠的兜底
        if (path == null || !path.startsWith("/")) {
            throw new IllegalArgumentException("站内路径必须以 / 开头，实际为：" + path);
        }
        return siteUrl + path;
    }
}
