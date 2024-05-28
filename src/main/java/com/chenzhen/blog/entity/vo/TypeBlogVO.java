package com.chenzhen.blog.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chenjixian
 * @date 2024/5/28 11:38
 * @description: 分类博文列表VO
 */
@Data
public class TypeBlogVO {

    private Long typeId;

    private String typeName;

    private List<BlogVO> blogList;

}
