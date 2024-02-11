package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.Comment;
import com.chenzhen.blog.entity.pojo.Message;
import com.chenzhen.blog.entity.pojo.User;
import com.chenzhen.blog.entity.vo.CommentVO;
import com.chenzhen.blog.mapper.BlogMapper;
import com.chenzhen.blog.entity.*;
import com.chenzhen.blog.service.CommentService;
import com.chenzhen.blog.mapper.CommentMapper;
import com.chenzhen.blog.util.MailUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author MIS
* @description 针对表【t_comment】的数据库操作Service实现
* @createDate 2022-09-11 18:21:11
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public PageInfo<CommentVO> pageCommentList(Long blogId,Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        PageHelper.orderBy("create_time desc");
        List<CommentVO> commentList = getBaseMapper().getRootCommentList(blogId);//获得所有根评论，未注入回复评论

        PageInfo<CommentVO> pageInfo = new PageInfo<>(commentList, 5);


        return pageInfo;
    }

    @Transactional
    @Override
    public int saveComment(Comment comment) {
        //更新博客的评论数量
        blogMapper.updateCommentCount(comment.getBlogId());
        return getBaseMapper().insert(comment);
    }




}




