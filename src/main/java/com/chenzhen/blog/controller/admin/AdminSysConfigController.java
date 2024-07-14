package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.pojo.SysConfig;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.vo.SysConfigVO;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.BeanUtils;
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
    public R page(){
        SysConfigVO sysConfigVO = sysConfigService.getSysConfig();
        return R.success().data("sysConfig",sysConfigVO);
    }

    @ResponseBody
    @PutMapping()
    public R update(@RequestBody SysConfigVO sysConfig) {
        return sysConfigService.updateSysConfig(sysConfig) ? R.success() : R.error("更新失败");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return sysConfigService.removeById(id) ? R.success() : R.error("删除失败");
    }



}
