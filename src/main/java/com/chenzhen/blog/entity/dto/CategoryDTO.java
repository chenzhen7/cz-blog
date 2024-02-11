package com.chenzhen.blog.entity.dto;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/7/12 23:09
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public class CategoryDTO {

    private Integer id;

    private String name;

    private boolean haveSubCategory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHaveSubCategory() {
        return haveSubCategory;
    }

    public void setHaveSubCategory(boolean haveSubCategory) {
        this.haveSubCategory = haveSubCategory;
    }
}
