package com.chenzhen.blog.controller;

import com.chenzhen.blog.entity.pojo.SysConfig;
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

        SysConfig sysConfig = sysConfigService.list().get(0);

        model.addAttribute("intro",sysConfig.getAboutMeContent());
        model.addAttribute("content",sysConfig.getAboutMeContent());
        model.addAttribute("skills",sysConfig.getAboutMeSkill().split("[,，]"));
        model.addAttribute("author",sysConfig.getAuthor());

        // 更新总浏览量
        viewsService.updateTotalViews();

        return "about";
    }
}
