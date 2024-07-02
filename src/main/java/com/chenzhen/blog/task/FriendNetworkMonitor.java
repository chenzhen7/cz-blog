package com.chenzhen.blog.task;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chenzhen.blog.entity.pojo.Friend;
import com.chenzhen.blog.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/2 21:25
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Component
@Slf4j
public class FriendNetworkMonitor {

    @Autowired
    private FriendService friendService;
    /**
     * 友链网络状态监控定时任务每日0点执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void execute() {
        log.info("友链网络状态监控定时任务执行时间：" + LocalDateTime.now());
        friendService.friendNetworkMonitorTask();
    }

}
