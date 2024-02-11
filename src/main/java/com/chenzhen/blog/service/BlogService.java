package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.dto.BlogDTO;
import com.chenzhen.blog.entity.vo.BlogVO;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_blog】的数据库操作Service
* @createDate 2022-09-11 18:21:11
*/
public interface BlogService extends IService<Blog> {

    //分页显示[博客管理列表]
    PageInfo<BlogVO> pageAdminBlogs(BaseQuery query);

    //分页显示[首页博客列表]
    PageInfo<BlogVO> pageIndex(Integer pageNum,Integer pageSize);

    Boolean updateBlogTags(Long blogId, List<Long> tagIds);

    Boolean saveBlogTags(Long id, List<Long> tagIds);

    //获取推荐列表
    List<BlogVO> getRecommendList();

    Blog getBlogDetail(Long id);

    //所有博客浏览量总数
    Long getBlogViewTotal();

    R saveBlog(BlogDTO blogDTO);

    BlogDTO getBlogDTO(Long id);

    R updateBlog(BlogDTO blog);

    PageInfo<BlogVO> pageTagsBlog(long[] tagIds, Integer pageNum, int pageSize);

    List<Blog> searchBlog(String keyword);

    List<Blog> getSimilarBlogs(Long id);
}
