package com.chenzhen.blog.controller;

import cn.hutool.core.util.ArrayUtil;
import com.chenzhen.blog.entity.vo.BlogVO;
import com.chenzhen.blog.entity.vo.TagVO;
import com.chenzhen.blog.service.BlogService;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.service.TagService;
import com.chenzhen.blog.service.ViewsService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/14 15:58
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private ViewsService viewsService;
    @Autowired
    private SysConfigService sysConfigService;
    //分类页面
    @GetMapping("/tags")
    public String types(@RequestParam(value = "tagIds",required = false) long[] tagIds,
                        @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                        Model model){

        List<TagVO> tagList = tagService.listTagsVO();
        //获得所有分类列表
        PageInfo<BlogVO> pageInfo = blogService.pageTagsBlog(tagIds, pageNum, 8);
        //将当前标签Id列表转为字符串，用逗号分隔，方便页面拼接url
        String tagIdsUrlString = ArrayUtil.isNotEmpty(tagIds)? ArrayUtil.join(tagIds,",") : "";
        // 获取作者
        String author = sysConfigService.list().get(0).getAuthor();

        model.addAttribute("tagList",tagList);
        model.addAttribute("page",pageInfo);
        //目前所在的tagIds添加到model中，使页面可以获取
        model.addAttribute("currTagIds",tagIdsUrlString);
        model.addAttribute("author",author);

        // 更新总浏览量
        viewsService.updateTotalViews();

        return "tags";
    }



}
