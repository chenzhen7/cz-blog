package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.WhiteList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.query.WhiteListQuery;
import com.github.pagehelper.PageInfo;

import java.util.Set;

/**
* @author MIS
* @description 针对表【t_while_list】的数据库操作Service
* @createDate 2024-08-04 18:18:41
*/
public interface WhiteListService extends IService<WhiteList> {

    void saveBlackList(String realIp);

    PageInfo<WhiteList> pageWhiteList(WhiteListQuery query);

    Set<String> getBlackList();
}
