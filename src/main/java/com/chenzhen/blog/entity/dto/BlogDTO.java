package com.chenzhen.blog.entity.dto;

import com.chenzhen.blog.entity.pojo.Blog;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/1/12 19:05
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class BlogDTO extends Blog {
    // 标签id列表
    private List<Long> tagIds;
}
