package com.chenzhen.blog.controller;

import com.chenzhen.blog.entity.annotation.BlogLog;
import com.chenzhen.blog.entity.vo.BlogVO;
import com.chenzhen.blog.service.BlogService;
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
 * @create 2022/9/15 22:08
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
public class ArchivesController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private ViewsService viewsService;
    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping("/archives")
    @BlogLog("归档页面")
    public String archives(Model model){

        // 获取归档文章列表。描述截断属于业务规则，由 service 负责，Controller 只做响应封装
        List<BlogVO> memoryList = blogService.getArchiveList();
        // 获取作者
        String author = sysConfigService.list().get(0).getAuthor();


        model.addAttribute("memoryList",memoryList);
        model.addAttribute("author",author);

        // 更新总浏览量
        viewsService.updateTotalViews();
        return "archives";
    }
}
