package com.chenzhen.blog.controller;

import com.chenzhen.blog.entity.enums.SysConfigEnum;
import com.chenzhen.blog.entity.vo.SkillVO;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.service.ViewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/17 21:22
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
public class AboutController {

    @Autowired
    ViewsService viewsService;
    @Autowired
    SysConfigService sysConfigService;

    @GetMapping("/about")
    public String about(Model model){

        // 获取简介
        String intro = sysConfigService.getByEnums(SysConfigEnum.ABOUT_ME_INTRODUCTION).getValue();
        // 获取内容
        String content = sysConfigService.getByEnums(SysConfigEnum.ABOUT_ME_CONTENT).getValue();
        // 获取作者
        String author = sysConfigService.getByEnums(SysConfigEnum.AUTHOR).getValue();
        // 获取技能
        List<SkillVO> skills = sysConfigService.getSkills();

        model.addAttribute("intro",intro);
        model.addAttribute("content",content);
        model.addAttribute("skills",skills);
        model.addAttribute("author",author);

        // 更新总浏览量
        viewsService.updateTotalViews();

        return "about";
    }
}
