package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.dto.FriendlinkAuditDTO;
import com.chenzhen.blog.entity.pojo.Friend;
import com.chenzhen.blog.entity.query.FriendlinkQuery;
import com.chenzhen.blog.service.FriendService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/17 11:52
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@CrossOrigin
@Controller
@RequestMapping("/admin/friendlinks")
public class AdminFriendLinkController {

    @Autowired
    private FriendService friendService;


    //跳转[友链管理]页面
    @GetMapping("")
    public String friendlinks(){
        return "admin/friendlinks";
    }

    @ResponseBody
    @GetMapping("/page")
    public R page(FriendlinkQuery query){
        return R.success().data("page",friendService.pageFriendLinks(query));
    }

    @ResponseBody
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.success().data("friendlink",friendService.getById(id));
    }

    @ResponseBody
    @PostMapping()
    public R save(@RequestBody @Valid Friend friend) {
        return friendService.save(friend) ? R.success() : R.error("新增失败");
    }

    @ResponseBody
    @PutMapping()
    public R update(@RequestBody @Valid Friend friend) {
        return friendService.updateById(friend) ? R.success() : R.error("更新失败");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return friendService.removeById(id) ? R.success() : R.error("删除失败");
    }

    @ResponseBody
    @GetMapping("/pass/{id}")
    public R pass(@PathVariable Long id) {
        return friendService.pass(id) ? R.success() : R.error("审核失败");
    }

    @ResponseBody
    @PostMapping("/reject")
    public R reject(@RequestBody FriendlinkAuditDTO auditDTO) {
        return friendService.reject(auditDTO) ? R.success() : R.error("拒绝失败");
    }



}
