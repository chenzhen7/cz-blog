package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.Mail;
import com.chenzhen.blog.entity.dto.FriendlinkAuditDTO;
import com.chenzhen.blog.entity.pojo.Friend;
import com.chenzhen.blog.entity.query.FriendlinkQuery;
import com.chenzhen.blog.service.FriendService;
import com.chenzhen.blog.mapper.FriendMapper;
import com.chenzhen.blog.util.MailUtil;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

/**
* @author MIS
* @description 针对表【t_friend】的数据库操作Service实现
* @createDate 2022-09-11 18:21:11
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

    @Autowired
    private MailUtil mailUtil;
    //自己的邮箱地址 从yaml配置文件中获取
    @Value("${spring.mail.username}")
    private String myEmail;

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

    @Override
    public R applyFriendLink(Friend friend) throws MessagingException {
        //保存申请记录
        save(friend);

        Mail mail = new Mail();
        mail.setFrom(myEmail)
                .setAcceptMailAccount(myEmail)
                .setName("ChenZhen")
                .setSubject("有人在您的博客申请了友链哦！");
        //发送邮件提醒我
        mailUtil.sendFriendLinkReminderEmail(mail);

        return R.success();
    }


}




