package com.chenzhen.blog.sdk;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/12/21 22:07
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
@Builder
@Slf4j
public class LinkSubmitRequest {

    /**
     * 在搜索资源平台验证的站点，一般就是你的博客网址首页例如：https://www.chenzhen.space
     */
    private String site;

    /**
     * token 密钥
     */
    private String token;

    /**
     * 要推送收录的URL列表
     */
    private List<String> urls;


}