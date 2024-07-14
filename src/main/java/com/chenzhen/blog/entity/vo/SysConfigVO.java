package com.chenzhen.blog.entity.vo;

import com.chenzhen.blog.entity.pojo.SysConfig;
import lombok.Data;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/11 23:26
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class SysConfigVO extends SysConfig {

    private String[] aboutMeSkills;
}
