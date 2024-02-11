package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.dto.UpdateUserPwdDTO;
import com.chenzhen.blog.entity.pojo.User;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.UserService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/2/8 15:08
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
@CrossOrigin
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String tag(){
        return "admin/users";
    }

    @ResponseBody
    @GetMapping("/page")
    public R page(BaseQuery query){
        return R.success().data("page",userService.pageUsers(query));
    }

    @ResponseBody
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return R.success().data("user",userService.getById(id));
    }

    @ResponseBody
    @PostMapping()
    public R save(@RequestBody User user) {
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        user.setAvatar("/images/me.jpg");
        return userService.save(user) ? R.success() : R.error("保存失败");
    }

    @ResponseBody
    @PutMapping()
    public R update(@RequestBody User user) {
        return userService.updateById(user) ? R.success() : R.error("更新失败");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return userService.removeById(id) ? R.success() : R.error("删除失败");
    }


    @ResponseBody
    @PostMapping("/updatePassword")
    //修改密码
    public R updatePassword(@RequestBody UpdateUserPwdDTO updateUserPwdDTO){
        return userService.updatePassword(updateUserPwdDTO);

    }

}
