package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.pojo.Tag;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.TagService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/1/11 14:47
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
@CrossOrigin
@RequestMapping("/admin/tags")
public class AdminTagController {

    @Autowired
    private TagService tagService;

    @GetMapping()
    public String tag(){
        return "admin/tags";
    }

    @ResponseBody
    @GetMapping("/page")
    public R page(BaseQuery query){
        return R.success().data("page",tagService.pageTags(query));
    }

    @ResponseBody
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return R.success().data("tag",tagService.getById(id));
    }

    @ResponseBody
    @PostMapping()
    public R save(@RequestBody Tag tag) {
        return tagService.save(tag) ? R.success() : R.error("保存失败");
    }

    @ResponseBody
    @PutMapping()
    public R update(@RequestBody Tag tag) {
        return tagService.updateById(tag) ? R.success() : R.error("更新失败");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return tagService.removeById(id) ? R.success() : R.error("删除失败");
    }

    @ResponseBody
    @GetMapping("/all")
    public R getAll(){
            return R.success().data("tagList",tagService.list());
        }



}
