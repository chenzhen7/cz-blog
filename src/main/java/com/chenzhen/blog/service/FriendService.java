package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.dto.FriendlinkAuditDTO;
import com.chenzhen.blog.entity.pojo.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.query.FriendlinkQuery;
import com.github.pagehelper.PageInfo;

/**
* @author MIS
* @description 针对表【t_friend】的数据库操作Service
* @createDate 2022-09-11 18:21:11
*/
public interface FriendService extends IService<Friend> {

    PageInfo<Friend> pageFriendLinks(FriendlinkQuery query);

    //审核通过
    Boolean pass(Long id);

    Boolean reject(FriendlinkAuditDTO auditDTO);
}
