package com.chenzhen.blog.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import com.chenzhen.blog.entity.dto.LoginDTO;
import com.chenzhen.blog.service.UserService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/11 18:42
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String loginPage(){
        return "admin/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public R login(@RequestBody @Valid LoginDTO loginDTO,HttpSession session){
        return userService.login(loginDTO,session);
    }

    @GetMapping("/logout")
    @ResponseBody
    public R logout(){
        StpUtil.logout();
        return R.success();
    }


//    @PostMapping("/login")
//    public String login(String username, String password, HttpSession session, RedirectAttributes attributes){
//        User user = userService.checkUser(username, password);
//        if(user !=null){
//            user.setPassword(null);
//            // 第1步，先登录上
//            StpUtil.login(10001);
//            // 第2步，获取 Token  相关参数
//            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
//            // 第3步，保存到session，传给前端
//
//            session.setAttribute("user",user);
//            session.setAttribute("tokenInfo", tokenInfo);
//            session.setMaxInactiveInterval(-1);     // 设置session永不过期
//            return "redirect:/admin/blogs";
//        }else{
//            attributes.addFlashAttribute("message","用户名和密码错误");
//            return "redirect:/admin";
//        }
//    }
//



}
