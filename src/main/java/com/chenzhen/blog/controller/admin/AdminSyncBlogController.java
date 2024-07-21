package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.dto.CheckSameBlogDTO;
import com.chenzhen.blog.entity.dto.SyncCsdnDTO;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.query.SyncBlogQuery;
import com.chenzhen.blog.service.SyncBlogService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/20 16:48
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
@CrossOrigin
@RequestMapping("/admin/sync-blogs")
public class AdminSyncBlogController {

    @Autowired
    private SyncBlogService syncBlogService;

    @GetMapping("")
    public String syncBlog(){
        return "admin/sync-blogs";
    }


    /**
     * csdn文章列表
     */
    @ResponseBody
    @GetMapping("/csdnList")
    public R csdnList(SyncBlogQuery query) throws Exception {
        return syncBlogService.pageCsdnBlogs(query);
    }
    /**
     * 查找是否有存在相同的文章
     */
    @ResponseBody
    @PostMapping("/checkSameBlog")
    public R checkSameBlog(@RequestBody CheckSameBlogDTO dto){
       return R.success().data("sameTitles",syncBlogService.checkSameBlog(dto.getTitles()));
    }
    /**
     * csdn文章同步接口
     */
    @ResponseBody
    @PostMapping("/syncCsdn")
    public R batchAyncCsdn(@RequestBody SyncCsdnDTO dto) throws Exception {
        syncBlogService.batchAyncCsdn(dto);
        return R.success();
    }
}
