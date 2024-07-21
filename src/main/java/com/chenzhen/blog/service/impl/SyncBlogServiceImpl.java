package com.chenzhen.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chenzhen.blog.entity.dto.SyncCsdnDTO;
import com.chenzhen.blog.entity.pojo.Blog;
import com.chenzhen.blog.entity.query.SyncBlogQuery;
import com.chenzhen.blog.exception.AdminException;
import com.chenzhen.blog.sdk.CommonResult;
import com.chenzhen.blog.sdk.csdn.*;
import com.chenzhen.blog.service.BlogService;
import com.chenzhen.blog.service.SyncBlogService;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/20 16:49
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Slf4j
@Service
public class SyncBlogServiceImpl implements SyncBlogService {

    @Autowired
    private BlogService blogService;
    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public R pageCsdnBlogs(SyncBlogQuery query) throws Exception {
        ListRequest listRequest = ListRequest.builder()
                .pageSize(query.getPageSize())
                .status(query.getStatus())
                .keyword(query.getKeyword())
                .page(query.getPageNum())
                .cookie(sysConfigService.getCsdnSession())
                .build();
        CommonResult<ListResp> commonResult = BizClient.list(listRequest);
        if (!commonResult.getSuccess()){
            R.error("获取CSDN博客失败");
        }
        ListResp listResp = commonResult.getResult();
        if (CollUtil.isEmpty(listResp.getList())) {
            return R.success().data("page",new PageInfo<>());
        }
        PageInfo<ListResp.Article> pageInfo = new PageInfo<>();

        pageInfo.setList(listResp.getList());
        pageInfo.setTotal(listResp.getTotal());
        pageInfo.setPages(listResp.getTotal() / listResp.getSize());
        pageInfo.setPageNum(listResp.getPage());
        pageInfo.setPageSize(listResp.getSize());

        return R.success().data("page",pageInfo);
    }

    /**
     *
     * @param titles
     * @return 返回相同的文章标题列表
     * 如果没有相同的文章，则返回null
     * 如果有2篇或以上的相同文章 则报错
     */
    @Override
    public List<String> checkSameBlog(List<String> titles) {
        List<Blog> sameBlogs = blogService.list(new LambdaQueryWrapper<Blog>()
                .in(Blog::getTitle, titles));
        if (CollUtil.isEmpty(sameBlogs)) {
            return null;
        }
        //找出相同超过2篇的文章
        List<String> duplicates = sameBlogs.stream()
                .collect(Collectors.groupingBy(Blog::getTitle))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() > 1)
                .map(e -> "《" + e.getKey() + "》")
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(duplicates)){
            return sameBlogs.stream().map(b -> "《" + b.getTitle() + "》").collect(Collectors.toList());
        }
        // 如果存在相同的文章超过1篇，则报错
        if (duplicates.size() > 0){
            throw new AdminException("文章" +
                    String.join("", duplicates) +
                    "存在相同的文章2篇以上，请修改后再同步");
        }
        return null;
    }

    @Override
    public void batchAyncCsdn(SyncCsdnDTO dto) throws Exception {
        for (Integer id : dto.getIds()) {
            AyncCsdn(dto.getTypeId(), id);
        }
    }

    private void AyncCsdn(Long typeId, Integer id) throws Exception {

        GetArticleRequest getArticleRequest = GetArticleRequest.builder()
                .id(id)
                .cookie(sysConfigService.getCsdnSession())
                .build();

        CommonResult<GetArticleResp> commonResult = BizClient.getArticle(getArticleRequest);
        if (!commonResult.getSuccess()){
            throw new AdminException("同步文章失败,文章ID：" + id);
        }
        GetArticleResp getArticleResp = commonResult.getResult();
        //寻找相同的文章
        List<Blog> blogs = blogService.list(new LambdaQueryWrapper<Blog>()
                .eq(Blog::getTitle, getArticleResp.getTitle().trim()));
        //如果没有相同的文章，则新增
        if (CollUtil.isEmpty(blogs)){
            syncNewBlog(typeId, getArticleResp);
        }
        //如果有相同的文章，则覆盖原文章内容
        else if (blogs.size() == 1){
            syncUpdateBlog(blogs.get(0), getArticleResp);
        }
        //如果有多个相同的文章，则报错
        else{
             throw new AdminException("当前文章标题存在相同的文章超过1篇，请修改后再同步");
        }

    }

    private void syncUpdateBlog(Blog blog, GetArticleResp getArticleResp) {
        blog.setContent(getArticleResp.getMarkdown_content());
        blogService.updateById(blog);
    }

    private void syncNewBlog(Long typeId, GetArticleResp getArticleResp) {
        Blog blog = new Blog();
        blog.setTitle(getArticleResp.getTitle());
        blog.setTypeId(typeId);
        blog.setContent(getArticleResp.getMarkdown_content());
        blog.setDescription(getArticleResp.getDescription());
        blog.setFirstPicture(CollUtil.isEmpty(getArticleResp.getCover_image()) ? null : getArticleResp.getCover_image().get(0));
        blog.setCommentabled(true);
        blog.setAppreciation(true);
        blog.setPublished(true);
        blog.setRecommend(false);
        blog.setShareStatement(true);
        blog.setCopyright(1);

        blogService.save(blog);
    }
}
