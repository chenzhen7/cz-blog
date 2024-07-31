package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.pojo.User;
import com.chenzhen.blog.entity.vo.CommentBlogVO;
import com.chenzhen.blog.entity.vo.CommentVO;
import com.github.pagehelper.PageInfo;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author MIS
* @description 针对表【t_comment】的数据库操作Service
* @createDate 2022-09-11 18:21:11
*/
public interface CommentService extends IService<Comment> {

    PageInfo<CommentVO> pageCommentList(Long blogId, Integer pageNum);

    int saveComment(Comment comment);

    List<CommentBlogVO> getRecentComment();

}
