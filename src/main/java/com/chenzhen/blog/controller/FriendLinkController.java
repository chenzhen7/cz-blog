package com.chenzhen.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chenzhen.blog.entity.pojo.Friend;
import com.chenzhen.blog.service.FriendService;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.service.ViewsService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/17 11:47
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller()
@RequestMapping("/friends")
public class FriendLinkController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private ViewsService viewsService;
    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping()
    public String friendLink(Model model){
        // 获取审核状态为已通过的友链
        List<Friend> list = friendService.list(new LambdaQueryWrapper<Friend>().eq(Friend::getStatus, Friend.Status.PASS));
        // 获取作者
        String author = sysConfigService.list().get(0).getAuthor();

        model.addAttribute("friendlinks",list);
        model.addAttribute("author",author);

        // 更新总浏览量
        viewsService.updateTotalViews();
        return "friends";
    }


    @PostMapping("/apply-friendlink")
    @ResponseBody
    public R applyFriendlink(Friend friend) throws MessagingException {
        return friendService.applyFriendLink(friend);
    }
}
