package com.chenzhen.blog.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_friend】的数据库操作Service实现
* @createDate 2022-09-11 18:21:11
*/
@Service
@Slf4j
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

    @Autowired
    private MailUtil mailUtil;
    //自己的邮箱地址 从yaml配置文件中获取
    @Value("${spring.mail.username}")
    private String myEmail;
    @Value("${spring.mail.friend-link-apply-template}")
    private String friendLinkApplyTemplate;
    @Value("${spring.mail.friend-link-pass-template}")
    private String friendLinkPassTemplate;
    @Value("${spring.mail.friend-link-reject-template}")
    private String friendLinkRejectTemplate;


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
        update(new LambdaUpdateWrapper<Friend>()
                .eq(Friend::getId,id)
                .set(Friend::getStatus,Friend.Status.PASS));
        Friend friend = getById(id);
        //没有留邮箱 直接返回
        if (StrUtil.isBlank(friend.getEmail())){
            return true;
        }
        Mail mail = new Mail();
        mail.setFrom(myEmail)
                .setAcceptMailAccount(friend.getEmail())
                .setName(friend.getBlogName())
                .setSubject("您的友链申请已成功通过审核！");
        mailUtil.sendFriendLinkReminderEmail(mail,friendLinkPassTemplate);
        return true;
    }

    @Override
    public Boolean reject(FriendlinkAuditDTO auditDTO) {
        update(new LambdaUpdateWrapper<Friend>()
                .eq(Friend::getId,auditDTO.getId())
                .set(Friend::getStatus,Friend.Status.REJECT)
                .set(Friend::getReason,auditDTO.getReason()));
        Friend friend = getById(auditDTO.getId());
        //没有留邮箱 直接返回
        if (StrUtil.isBlank(friend.getEmail())){
            return true;
        }
        Mail mail = new Mail();
        mail.setFrom(myEmail)
                .setAcceptMailAccount(friend.getEmail())
                .setName(friend.getBlogName())
                .setSubject("您的友链申请没有通过审核！")
                .setReply(auditDTO.getReason());

        mailUtil.sendFriendLinkReminderEmail(mail,friendLinkRejectTemplate);
        return true;

    }

    @Override
    public R applyFriendLink(Friend friend){
        //保存申请记录
        save(friend);

        Mail mail = new Mail();
        mail.setFrom(myEmail)
                .setAcceptMailAccount(myEmail)
                .setName("ChenZhen")
                .setSubject("有人在您的博客申请了友链哦！");
        //发送邮件提醒我
        mailUtil.sendFriendLinkReminderEmail(mail,friendLinkApplyTemplate);

        return R.success();
    }

    @Override
    public void friendNetworkMonitorTask() {

        List<Friend> friends = list(new LambdaQueryWrapper<Friend>().eq(Friend::getStatus, Friend.Status.PASS));
        for (Friend friend : friends) {
            //获取网络状态
            Integer netStatus = getNetStatus(friend);
            log.info("友链={},网络状态={}", friend.getBlogName(), netStatus);
            //更新状态
            friend.setNetStatus(netStatus);
            updateById(friend);
        }
    }

    /**
     * 获取友链网络状态
     * @param friend
     * @return
     */
    private Integer getNetStatus(Friend friend) {
        HttpResponse response = null;
        try {
            long start = System.currentTimeMillis();
            response = HttpRequest
                    .get(friend.getBlogAddress())
                    .timeout(30 * 1000)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36")
                    .header("Accept" , "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .execute();
            long end = System.currentTimeMillis();
            long cost = end - start;
            log.info("友链={},访问耗时={}", friend.getBlogName(), cost + "ms");
            if (response == null) {
                return Friend.NetStatus.ERROR;
            }
            //处理重定向请求
            if (response.getStatus() == HttpStatus.HTTP_MOVED_TEMP || response.getStatus() == HttpStatus.HTTP_MOVED_PERM
                    || response.getStatus() == HttpStatus.HTTP_SEE_OTHER || response.getStatus() == HttpStatus.HTTP_TEMP_REDIRECT
                    || response.getStatus() ==HttpStatus.HTTP_PERMANENT_REDIRECT ) {
                response = executeRedirect(friend.getBlogName(), response);
            }
            if (response == null || response.getStatus() != 200) {
                return Friend.NetStatus.ERROR;
            }
            //小于5秒
            if (cost < 5 * 1000) {
                return Friend.NetStatus.GOOD;
            }
            //大于5 小于10秒
            if (cost < 10 * 1000) {
                return Friend.NetStatus.SLOW;
            }
            //大于10 小于30秒
            if (cost < 30 * 1000) {
                return Friend.NetStatus.TIMEOUT;
            }
        }catch (Exception e) {
            log.error("友链={}，访问出现异常,response={}", friend.getBlogName(),response);
            e.printStackTrace();

            return Friend.NetStatus.ERROR;
        }

        return Friend.NetStatus.ERROR;
    }

    private static HttpResponse executeRedirect(String blogName,HttpResponse response) {

            String newUrl = response.header("Location");
            log.info("友链：{}，重定向到: {}", blogName,newUrl);
            response = HttpRequest
                    .get(newUrl)
                    .timeout(30 * 1000)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36")
                    .header("Accept" , "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .execute();

            return response;
    }




}




