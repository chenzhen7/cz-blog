package com.chenzhen.blog.entity.vo;

import com.chenzhen.blog.entity.pojo.Comment;
import lombok.Data;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/7 14:43
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class CommentBlogVO extends Comment {

    private String blogTitle;
}
