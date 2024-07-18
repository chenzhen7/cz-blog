package com.chenzhen.blog.sdk.csdn;

/**
 * @author chenjixian
 * @date 2024/7/18 10:17
 * @description:
 */
public interface BizApi {

    /**
     * host
     */
    String HOST = "https://bizapi.csdn.net";

    /**
     * 文章列表接口
     */
    String LIST_URL  = "/blog/phoenix/console/v1/article/list";

    /**
     * 获取文章详情
     */
    String GET_ARTICLE_URL  = "/blog-console-api/v3/editor/getArticle";
}
