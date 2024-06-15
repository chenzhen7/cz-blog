package com.chenzhen.blog.entity.query;

import lombok.Data;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/6/10 19:31
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class BlogQuery extends BaseQuery{

    private String title;

    private String typeName;

    private String tagName;

    private Integer published;

    private Integer recommend;

    private Integer copyright;


}
