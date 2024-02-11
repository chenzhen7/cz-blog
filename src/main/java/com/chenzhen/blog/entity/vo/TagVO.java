package com.chenzhen.blog.entity.vo;

import com.chenzhen.blog.entity.pojo.Tag;
import lombok.Data;

@Data
public class TagVO extends Tag {
    //该标签的博客数量
    private Integer blogCount;
}
