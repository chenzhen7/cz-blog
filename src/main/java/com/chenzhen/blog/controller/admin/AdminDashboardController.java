package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.pojo.Blog;
import com.chenzhen.blog.entity.pojo.Message;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.query.BlogQuery;
import com.chenzhen.blog.entity.vo.CommentBlogVO;
import com.chenzhen.blog.service.*;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/7 14:25
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
@CrossOrigin
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    ViewsService viewsService;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentService commentService;
    @Autowired
    MessageService messageService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    SysLogService sysLogService;

    @GetMapping("")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/info")
    @ResponseBody
    public R info() {
        // 获取博客总浏览量
        Long blogViewTotal = viewsService.getTotalViews();
        // 获取博客昨日浏览量
        Long blogViewYesterday = viewsService.getYesterdayViews();
        // 获取博客总数
        Long blogTotal = blogService.count();
        // 获取博客分类总数
        Long blogTypeTotal = typeService.count();
        // 获取博客标签总数
        Long blogTagTotal = tagService.count();
        // 获取评论总数
        Long blogCommentTotal = commentService.count();
        // 获取留言总数
        Long blogMessageTotal = messageService.count();

        //文章访问量Top10
        List<Blog> blogViewTop10 = blogService.getBlogViewTop10();
        //最近评论
        List<CommentBlogVO> recentComment = commentService.getRecentComment();
        //最近留言
        List<Message> recentMessage = messageService.getRecentMessage();


        return R.success()
                .data("blogTotal", blogTotal)
                .data("blogTypeTotal", blogTypeTotal)
                .data("blogTagTotal", blogTagTotal)
                .data("blogCommentTotal", blogCommentTotal)
                .data("blogMessageTotal", blogMessageTotal)
                .data("blogViewTotal", blogViewTotal)
                .data("blogViewYesterday", blogViewYesterday)
                .data("blogViewTop10", blogViewTop10)
                .data("recentComment", recentComment)
                .data("recentMessage", recentMessage);

    }



}


