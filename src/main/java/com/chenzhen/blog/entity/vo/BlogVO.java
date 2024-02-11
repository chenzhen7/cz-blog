package com.chenzhen.blog.entity.vo;

import com.chenzhen.blog.entity.pojo.Blog;
import com.chenzhen.blog.entity.pojo.Tag;
import lombok.Data;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/1/12 16:38
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class BlogVO extends Blog {
    //标签名
    private List<Tag> tagList;

}
