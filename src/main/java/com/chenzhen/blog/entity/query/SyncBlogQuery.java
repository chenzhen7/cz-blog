package com.chenzhen.blog.entity.query;

import com.chenzhen.blog.sdk.csdn.ListRequest;
import lombok.Data;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/20 17:44
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class SyncBlogQuery extends BaseQuery{

    /**
     * 关键词
     */
    private String keyword;
    /**
     * 状态
     * @see ListRequest.Status
     */
    private String status;


}
