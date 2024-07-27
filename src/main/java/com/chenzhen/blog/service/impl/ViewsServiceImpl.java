package com.chenzhen.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.Views;
import com.chenzhen.blog.service.ViewsService;
import com.chenzhen.blog.mapper.ViewsMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
* @author Administrator
* @description 针对表【t_views】的数据库操作Service实现
* @createDate 2024-02-02 22:39:49
*/
@Service
public class ViewsServiceImpl extends ServiceImpl<ViewsMapper, Views>
    implements ViewsService{

    @Override
    public Long getTotalViews() {
        List<Views> list = list();
        if (CollUtil.isEmpty(list)){
            return 0L;
        }
        return list.get(0).getTotalViews();
    }

    @Override
    public Long getYesterdayViews() {
        List<Views> list = list();
        if (CollUtil.isEmpty(list)){
            return 0L;
        }
        return list.get(0).getYesterdayViews();
    }

    @Override
    public void updateTotalViews() {
        List<Views> list = list();
        if (CollUtil.isEmpty(list)){
            // 如果数据库中没有数据，说明是第一次更新，初始化总浏览量为1，昨日浏览量为0
            Views views = new Views();
            views.setTotalViews(1L);
            views.setYesterdayViews(0L);
            save(views);
            return;
        }
        Views views = list.get(0);
        // 获取最后一次更新时间
        LocalDateTime lastUpdateDate = views.getUpdateTime();
        // 表示今天午夜零点的时间
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        // 如果最后一次更新时间是昨天或更久，则将总浏览量的值更新到昨日浏览量
        if (lastUpdateDate.isBefore(todayStart)) {
            update(new LambdaUpdateWrapper<Views>()
                    .eq(Views::getId, views.getId())
                    .set(Views::getYesterdayViews, views.getTotalViews()));
        }
        //总浏览量 + 1
        update(new LambdaUpdateWrapper<Views>()
                .eq(Views::getId, views.getId())
                .set(Views::getTotalViews, views.getTotalViews() + 1L)
                .set(Views::getUpdateTime, LocalDateTime.now()));
    }
}




