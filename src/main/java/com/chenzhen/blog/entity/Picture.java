package com.chenzhen.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName t_picture
 */
@TableName(value ="t_picture")
@Data
public class Picture implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private String pictureAddress;
    private String pictureDescription;
    private String pictureName;
    private String pictureTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     */
    public String getPictureAddress() {
        return pictureAddress;
    }

    /**
     * 
     */
    public void setPictureAddress(String pictureAddress) {
        this.pictureAddress = pictureAddress;
    }

    /**
     * 
     */
    public String getPictureDescription() {
        return pictureDescription;
    }

    /**
     * 
     */
    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    /**
     * 
     */
    public String getPictureName() {
        return pictureName;
    }

    /**
     * 
     */
    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    /**
     * 
     */
    public String getPictureTime() {
        return pictureTime;
    }

    /**
     * 
     */
    public void setPictureTime(String pictureTime) {
        this.pictureTime = pictureTime;
    }
}