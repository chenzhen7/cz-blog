package com.chenzhen.blog.entity.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/6/10 17:19
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class BlogTag {

    private Long blogId;

    private List<Tag> tags;
}
