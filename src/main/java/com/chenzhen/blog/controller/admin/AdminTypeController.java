package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.pojo.Type;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.TypeService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenjixian
 * @date 2024/5/28 11:16
 * @description:
 */
@Controller
@CrossOrigin
@RequestMapping("/admin/types")
public class AdminTypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping()
    public String type(){
        return "admin/types";
    }

    @ResponseBody
    @GetMapping("/page")
    public R page(BaseQuery query){
        return R.success().data("page",typeService.pageTypes(query));
    }

    @ResponseBody
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return R.success().data("type",typeService.getById(id));
    }

    @ResponseBody
    @PostMapping()
    public R save(@RequestBody Type type) {
        return typeService.save(type) ? R.success() : R.error("保存失败");
    }

    @ResponseBody
    @PutMapping()
    public R update(@RequestBody Type type) {
        return typeService.updateById(type) ? R.success() : R.error("更新失败");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return typeService.removeById(id) ? R.success() : R.error("删除失败");
    }

    @ResponseBody
    @GetMapping("/all")
    public R getAll(){
        return R.success().data("typeList",typeService.list());
    }
}
