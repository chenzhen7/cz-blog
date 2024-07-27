package com.chenzhen.blog.controller;

import com.chenzhen.blog.entity.annotation.BlogLog;
import com.chenzhen.blog.entity.pojo.Comment;
import com.chenzhen.blog.entity.pojo.User;
import com.chenzhen.blog.entity.vo.CommentVO;
import com.chenzhen.blog.service.CommentService;
import com.chenzhen.blog.service.EmailService;
import com.chenzhen.blog.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/13 11:18
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private EmailService emailService;

    //查询评论列表
    @GetMapping("/comments/{blogId}")
    @CrossOrigin
    public String comments(@PathVariable Long blogId, Model model,
                           @RequestParam(defaultValue = "1") Integer pageNum) {
        //获取所有评论
        PageInfo<CommentVO> commentList = commentService.pageCommentList(blogId,pageNum);
        model.addAttribute("commentPageInfo", commentList);
        return "blog :: commentList";
    }

    //新增评论
    @PostMapping("/comments")
    @BlogLog("新增评论")
    public String post(Comment comment, HttpSession session,Integer pageNum) throws MessagingException {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }
        commentService.saveComment(comment);

        emailService.sendMail(user, comment);

        return "redirect:/comments/"+comment.getBlogId() + "?pageNum=" + pageNum;

    }

    //删除评论
    @GetMapping("/comments/delete/{id}")
    @BlogLog("删除评论")
    public String delete(@PathVariable("id")Long commentId,
                         Long blogId, Integer pageNum, HttpSession session){

        User user = (User)session.getAttribute("user");
        if(user != null){
            commentService.removeById(commentId);
        }
        return "redirect:/blog/"+ blogId;
    }

}
