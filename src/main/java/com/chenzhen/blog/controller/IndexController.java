package com.chenzhen.blog.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenzhen.blog.entity.enums.SysConfigEnum;
import com.chenzhen.blog.entity.pojo.Music;
import com.chenzhen.blog.entity.pojo.Type;
import com.chenzhen.blog.entity.vo.CommentVO;
import com.chenzhen.blog.entity.pojo.Blog;
import com.chenzhen.blog.entity.vo.BlogVO;
import com.chenzhen.blog.service.*;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/11 15:43
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ViewsService viewsService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private TypeService typeService;


    //跳转首页
    @GetMapping()
    public String index(@RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                        @RequestParam(value = "type",required = false)Long type,
                        Model model){

        //获取所有分类
        List<Type> typeList = typeService.getTypeList();
        //如果没有传分类Id 默认选第一个分类中的博文列表
        if (type == null){
            if (CollUtil.isNotEmpty(typeList)){
                type = typeList.get(0).getId();
            }
        }
        //获取首页博客列表分页信息
        PageInfo<BlogVO> pageInfo = blogService.pageIndex(type, pageNum, 12);
        //获取推荐列表
        List<BlogVO> recommendList = blogService.getRecommendList();
        //获取音乐列表
        List<Music> musicList = musicService.list();
        // 获取作者
        String author = sysConfigService.getByEnums(SysConfigEnum.AUTHOR).getValue();
        // 获取个人简介
        String profile = sysConfigService.getByEnums(SysConfigEnum.SITE_PROFILE).getValue();
        // 获取位置
        String location = sysConfigService.getByEnums(SysConfigEnum.SITE_LOCATION).getValue();
        // 获取邮箱
        String email = sysConfigService.getByEnums(SysConfigEnum.SITE_EMAIL).getValue();
        // 获取QQ
        String qq = sysConfigService.getByEnums(SysConfigEnum.SITE_QQ).getValue();
        // 获取微信
        String wechat = sysConfigService.getByEnums(SysConfigEnum.SITE_WECHAT).getValue();
        // 获取博客今日总浏览量
        Long blogViewTotal = viewsService.getTotalViews();
        // 获取博客昨日浏览量
        Long blogViewYesterday = viewsService.getYesterdayViews();
        // 获取博客总数
        Long blogTotal = blogService.count();
        // 获取评论总数
        Long blogCommentTotal = commentService.count();
        // 获取留言总数
        Long blogMessageTotal = messageService.count();

        model.addAttribute("typeList",typeList);
        model.addAttribute("nowType",type);
        model.addAttribute("page",pageInfo);
        model.addAttribute("recommendList",recommendList);
        model.addAttribute("musicList",musicList);
        model.addAttribute("author",author);
        model.addAttribute("profile",profile);
        model.addAttribute("location",location);
        model.addAttribute("email",email);
        model.addAttribute("qq",qq);
        model.addAttribute("wechat",wechat);

        model.addAttribute("blogViewTotal",blogViewTotal);
        model.addAttribute("blogViewYesterday",blogViewYesterday);
        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("blogCommentTotal",blogCommentTotal);
        model.addAttribute("blogMessageTotal",blogMessageTotal);

        // 更新总浏览量
        viewsService.updateTotalViews();

        return "index";
    }

    //跳转博客详情页
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable("id")Long id,Model model){
        //获取文章详情
        Blog blog = blogService.getBlogDetail(id);
        //获取相似推荐文章
        List<Blog> similarBlogs = blogService.getSimilarBlogs(blog.getId());
        //获取所有评论
        PageInfo<CommentVO> commentList = commentService.pageCommentList(id,1);
        // 获取作者
        String author = sysConfigService.getByEnums(SysConfigEnum.AUTHOR).getValue();


        model.addAttribute("blog",blog);
        model.addAttribute("similarBlogs",similarBlogs);
        model.addAttribute("commentPageInfo", commentList);
        model.addAttribute("author",author);

        // 更新总浏览量
        viewsService.updateTotalViews();
        return "blog";
    }

    //搜索
    @ResponseBody
    @GetMapping("/search")
    public R search(String keyword){
        //根据标题模糊搜索
        List<Blog> blogs = blogService.searchBlog(keyword.trim());
        return R.success().data("blogs",blogs);

    }
}
