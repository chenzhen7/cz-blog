package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.pojo.WhiteList;
import com.chenzhen.blog.entity.query.WhiteListQuery;
import com.chenzhen.blog.service.WhiteListService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/8/4 21:08
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
@CrossOrigin
@RequestMapping("/admin/whiteList")
public class AdminWhiteListController {

    @Autowired
    private WhiteListService whiteListService;

    @GetMapping("/page")
    @ResponseBody
    public R pageSysLogs(WhiteListQuery query) {
        return R.success().data("page", whiteListService.pageWhiteList(query));
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return whiteListService.removeById(id) ? R.success() : R.error("删除失败");
    }

    @ResponseBody
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return R.success().data("whiteList",whiteListService.getById(id));
    }


    @ResponseBody
    @PutMapping()
    public R update(@RequestBody WhiteList whiteList) {
        return whiteListService.updateById(whiteList) ? R.success() : R.error("更新失败");
    }




}
