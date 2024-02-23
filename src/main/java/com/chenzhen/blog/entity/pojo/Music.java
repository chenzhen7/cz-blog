package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName t_music
 */
@TableName(value ="t_music")
@Data
public class Music extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 音乐标题
     */
    private String title;

    /**
     * 艺术家或歌手名
     */
    private String artist;

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 音乐文件路径
     */
    private String filePath;

    /**
     * 封面路径
     */
    private String coverPath;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}