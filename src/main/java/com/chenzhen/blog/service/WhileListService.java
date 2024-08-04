package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.WhileList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.query.WhiteListQuery;
import com.github.pagehelper.PageInfo;

/**
* @author MIS
* @description 针对表【t_while_list】的数据库操作Service
* @createDate 2024-08-04 18:18:41
*/
public interface WhileListService extends IService<WhileList> {

    void saveBlackList(String realIp);

    PageInfo<WhileList> pageWhiteList(WhiteListQuery query);
}
