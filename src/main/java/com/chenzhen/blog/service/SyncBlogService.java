package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.dto.SyncCsdnDTO;
import com.chenzhen.blog.entity.query.SyncBlogQuery;
import com.chenzhen.blog.sdk.csdn.ListResp;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/20 16:49
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public interface SyncBlogService {
    void batchAyncCsdn(SyncCsdnDTO dto) throws Exception;

    R pageCsdnBlogs(SyncBlogQuery query) throws Exception;

    List<String> checkSameBlog(List<String> titles);
}
