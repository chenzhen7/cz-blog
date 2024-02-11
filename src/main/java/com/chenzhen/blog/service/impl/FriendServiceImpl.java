package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.dto.FriendlinkAuditDTO;
import com.chenzhen.blog.entity.pojo.Friend;
import com.chenzhen.blog.entity.query.FriendlinkQuery;
import com.chenzhen.blog.service.FriendService;
import com.chenzhen.blog.mapper.FriendMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_friend】的数据库操作Service实现
* @createDate 2022-09-11 18:21:11
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

    @Override
    public PageInfo<Friend> pageFriendLinks(FriendlinkQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        PageHelper.orderBy("create_time desc");
        List<Friend> list = baseMapper.pageFriendLinks(query);
        PageInfo<Friend> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public Boolean pass(Long id) {
        LambdaUpdateWrapper<Friend> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Friend::getId,id)
                .set(Friend::getStatus,Friend.Status.PASS);
        return update(wrapper);
    }

    @Override
    public Boolean reject(FriendlinkAuditDTO auditDTO) {
        LambdaUpdateWrapper<Friend> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Friend::getId,auditDTO.getId())
                .set(Friend::getStatus,Friend.Status.REJECT)
                .set(Friend::getReason,auditDTO.getReason());

        return update(wrapper);
    }


}




