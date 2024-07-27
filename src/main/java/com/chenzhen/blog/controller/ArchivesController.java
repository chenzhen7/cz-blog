package com.chenzhen.blog.controller;

import com.chenzhen.blog.mapper.BlogMapper;
import com.chenzhen.blog.entity.vo.BlogVO;
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
    private BlogMapper blogMapper;
    @Autowired
    private ViewsService viewsService;
    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping("/archives")
    public String archives(Model model){

        List<BlogVO> memoryList = blogMapper.getBlogList();
        for (BlogVO blogVO : memoryList) {
            //如果描述太长 截取前100个字
            if (blogVO.getDescription().length() > 100){
                blogVO.setDescription(blogVO.getDescription().substring(0,100) + "...");
            }
        }
        // 获取作者
        String author = sysConfigService.list().get(0).getAuthor();


        model.addAttribute("memoryList",memoryList);
        model.addAttribute("author",author);

        // 更新总浏览量
        viewsService.updateTotalViews();
        return "archives";
    }
}
