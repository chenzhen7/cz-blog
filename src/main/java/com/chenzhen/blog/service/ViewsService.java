package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.Views;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_views】的数据库操作Service
* @createDate 2024-02-02 22:39:49
*/
public interface ViewsService extends IService<Views> {

    Long getTotalViews();

    Long getYesterdayViews();

    void updateTotalViews();
}
