package com.chenzhen.blog.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

public enum SysConfigEnum {

    /**
     * 关于我页面-自我介绍
     */
    ABOUT_ME_INTRODUCTION("about_me_introduction", "关于我页面-自我介绍"),

    /**
     * 关于我页面-我的标签
     */
    ABOUT_ME_TAG("about_me_tag", "关于我页面-我的标签"),

    /**
     * 关于我页面-内容
     */
    ABOUT_ME_CONTENT("about_me_content", "关于我页面-内容"),

    /**
     * 站长名称
     */
    AUTHOR("author", "站长名称"),

    /**
     * 站点概览-个人简介
     */
    SITE_PROFILE("site_profile", "站点概览-个人简介"),

    /**
     * 站点概览-位置
     */
    SITE_LOCATION("site_location", "站点概览-位置"),

    /**
     * 站点概览-邮箱
     */
    SITE_EMAIL("site_email", "站点概览-邮箱"),

    /**
     *  站点概览-QQ
     */
    SITE_QQ("site_qq", "站点概览-QQ"),

    /**
     *  站点概览-微信
     */
    SITE_WECHAT("site_wechat", "站点概览-微信");


    private final String name;
    private final String description;


    SysConfigEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
