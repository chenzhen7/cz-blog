package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 系统配置表
 * @TableName t_sys_config
 */
@TableName(value ="t_sys_config")
@Data
public class SysConfig implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 站长名称
     */
    private String author;

    /**
     * 关于我页面-自我介绍
     */
    private String aboutMeIntroduction;

    /**
     * 关于我页面-内容
     */
    private String aboutMeContent;

    /**
     * 关于我页面-我的技能
     */
    private String aboutMeSkill;

    /**
     * 站点概览-个人简介
     */
    private String siteProfile;

    /**
     * 站点概览-位置
     */
    private String siteLocation;

    /**
     * 站点概览-邮箱
     */
    private String siteEmail;

    /**
     * 站点概览-QQ
     */
    private String siteQq;

    /**
     * 站点概览-微信
     */
    private String siteWechat;

    /**
     * CSDN的Session
     */
    private String csdnSession;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}