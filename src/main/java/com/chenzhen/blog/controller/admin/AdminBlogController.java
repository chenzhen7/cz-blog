package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.dto.BlogDTO;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.query.BlogQuery;
import com.chenzhen.blog.service.BlogService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/11 19:00
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
@CrossOrigin
@RequestMapping("/admin/blogs")
public class AdminBlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("")
    public String blog(){
        return "admin/blogs";
    }

    //跳转[博客列表]页面
    @ResponseBody
    @GetMapping("/page")
    public R blogs(BlogQuery query){
        return R.success().data("page",blogService.pageAdminBlogs(query));
    }

    @ResponseBody
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.success().data("blog",blogService.getBlogDTO(id));
    }

    @ResponseBody
    @PostMapping()
    public R save(@RequestBody @Valid BlogDTO blogDTO) {
        return blogService.saveBlog(blogDTO);
    }

    @ResponseBody
    @PutMapping()
    public R update(@RequestBody BlogDTO blog) {
        return blogService.updateBlog(blog);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return blogService.removeById(id) ? R.success() : R.error("删除失败");
    }



}
