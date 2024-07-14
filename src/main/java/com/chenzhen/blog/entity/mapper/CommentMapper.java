package com.chenzhen.blog.entity.mapper;

import com.chenzhen.blog.entity.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenzhen.blog.entity.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_comment】的数据库操作Mapper
* @createDate 2022-09-11 18:21:11
* @Entity com.chenzhen.blog.pojo.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {


    List<CommentVO> getRootCommentList(@Param("blogId") Long blogId);

}




