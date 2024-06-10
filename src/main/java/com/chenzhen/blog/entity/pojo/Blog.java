package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 
 * @TableName t_blog
 */
@TableName(value ="t_blog")
@Data
public class Blog extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类ID
     */
    private Long typeId;

    /**
     * 博文标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 博文内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 博文描述
     */
    @NotBlank(message = "描述不能为空")
    private String description;

    /**
     * 博文首图
     */
    @NotBlank(message = "首图不能为空")
    private String firstPicture;

    /**
     * 是否允许评论
     */
    @NotNull(message = "是否允许评论不能为空")
    private Boolean commentabled;

    /**
     * 是否开启赞赏
     */
    @NotNull(message = "是否开启赞赏不能为空")
    private Boolean appreciation;

    /**
     * 是否发布
     */
    private Boolean published;

    /**
     * 是否推荐
     */
    @NotNull(message = "推荐不能为空")
    private Boolean recommend;

    /**
     * 是否开启转载声明
     */
    @NotNull(message = "转载声明不能为空")
    private Boolean shareStatement;

    /**
     * 版权类型
     */
    @NotNull(message = "版权类型不能为空")
    private Integer copyright;

    /**
     * 浏览次数
     */
    private Integer views;

    /**
     * 评论数量
     */
    private Integer commentCount;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}