package com.chenzhen.blog.controller;


import com.chenzhen.blog.entity.annotation.BlogLog;
import com.chenzhen.blog.entity.vo.MessageVO;
import com.chenzhen.blog.entity.pojo.Message;
import com.chenzhen.blog.entity.pojo.User;
import com.chenzhen.blog.service.EmailService;
import com.chenzhen.blog.service.MessageService;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.service.ViewsService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/15 22:53
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
public class MessageController {
    @Autowired
    private ViewsService viewsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private EmailService emailService;


    //显示留言板页面
    @GetMapping(value = "/message")
    @BlogLog(value = "留言板页面")
    public String message(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {

        PageInfo<MessageVO> pageInfo = messageService.getMessageList(pageNum);
        // 获取作者
        String author = sysConfigService.list().get(0).getAuthor();

        model.addAttribute("page",pageInfo);
        model.addAttribute("author",author);
        // 更新总浏览量
        viewsService.updateTotalViews();
        return "message";
    }

    //查询评论列表
    @CrossOrigin
    @GetMapping(value = {"/messagecomment","/messagecomment/{pageNum}"})
    @BlogLog(value = "留言板评论列表")
    public String messageComment(@PathVariable(value = "pageNum",required = false) Integer pageNum, Model model) {
        if (pageNum == null){
            pageNum = 1;
        }
        PageInfo<MessageVO> pageInfo = messageService.getMessageList(pageNum);
        model.addAttribute("page",pageInfo);

        return "message :: messageList";
    }

    //新增留言
    @PostMapping("/message")
    @BlogLog(value = "新增留言")
    public String message(Message message, HttpSession session, Integer pageNum) throws MessagingException {

        User user = (User) session.getAttribute("user");
        //如果是管理员，设置管理员的权限
        if (user != null) {
            message.setAvatar(user.getAvatar());
            message.setAdminMessage(true);
        }
        //将评论信息保存数据库
        messageService.save(message);
        //异步发送评论提醒邮件
        emailService.sendMail(user, message);

        return "redirect:/messagecomment/"+pageNum;


    }

    //删除评论
    @GetMapping("/message/delete/{id}")
    public String delete(@PathVariable("id") Long id,HttpSession session,Integer pageNum){
        User user = (User) session.getAttribute("user");
        //如果是管理员
        if(user != null){
            messageService.removeById(id);
        }
        return "redirect:/message?pageNum="+pageNum;

    }



}
