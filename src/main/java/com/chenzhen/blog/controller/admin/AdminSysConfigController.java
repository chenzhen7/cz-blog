package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.pojo.SysConfig;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/admin/sys-configs")
public class AdminSysConfigController {

    @Autowired
    SysConfigService sysConfigService;

    @GetMapping("")
    public String blog(){
        return "admin/sys-configs";
    }

    @ResponseBody
    @GetMapping("/page")
    public R page(BaseQuery query){
        return R.success().data("page",sysConfigService.pageSysConfigs(query));
    }

    @ResponseBody
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.success().data("sysConfig",sysConfigService.getById(id));
    }

    @ResponseBody
    @PostMapping()
    public R save(@RequestBody @Valid SysConfig sysConfig) {
        return sysConfigService.save(sysConfig) ? R.success() : R.error("新增失败");
    }

    @ResponseBody
    @PutMapping()
    public R update(@RequestBody SysConfig sysConfig) {
        return sysConfigService.updateById(sysConfig) ? R.success() : R.error("更新失败");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return sysConfigService.removeById(id) ? R.success() : R.error("删除失败");
    }



}
