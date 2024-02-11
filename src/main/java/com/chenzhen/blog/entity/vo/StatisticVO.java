package com.chenzhen.blog.entity.vo;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/5/16 15:12
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public class StatisticVO {
    private Integer countPrompt;
    private Integer countUsage;
    private Integer countViews;
    private Integer countCategory;

    public Integer getCountUsage() {
        return countUsage;
    }

    public void setCountUsage(Integer countUsage) {
        this.countUsage = countUsage;
    }

    public Integer getCountViews() {
        return countViews;
    }

    public void setCountViews(Integer countViews) {
        this.countViews = countViews;
    }

    public Integer getCountCategory() {
        return countCategory;
    }

    public void setCountCategory(Integer countCategory) {
        this.countCategory = countCategory;
    }

    public Integer getCountPrompt() {
        return countPrompt;
    }

    public void setCountPrompt(Integer countPrompt) {
        this.countPrompt = countPrompt;
    }
}
