package com.chenzhen.blog.sdk.csdn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author chenjixian
 * @date 2024/7/18 10:20
 * @description:
 */
@Data
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListResp implements Serializable {

    private Count count;

    private java.util.List<Article> list;

    private Integer page;

    private Integer size;

    private Integer total;

    @Data
    public static class Count {
        private Integer all;
        private Integer deleted;
        private Integer original;
        private Integer enabled;
        private Integer audit;
        private Integer draft;
    }

    @Data
    public static class Article {
        /**
         * 文章ID
         */
        private String articleId;

        /**
         * 文章标题
         */
        private String title;

        /**
         * 文章封面
         */
        private java.util.List<String> coverImage;

        /**
         * 文章发布时间
         */
        private LocalDateTime postTime;

        /**
         * 文章浏览数
         */
        private int viewCount;

        /**
         * 文章评论数
         */
        private int commentCount;

        /**
         * 用户名
         */
        private String username;

        /**
         * 质量规范链接
         */
        private String qualitySpecification;
    }
}
