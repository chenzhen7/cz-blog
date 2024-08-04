package com.chenzhen.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.SysLog;
import com.chenzhen.blog.entity.pojo.WhileList;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.query.WhiteListQuery;
import com.chenzhen.blog.service.WhileListService;
import com.chenzhen.blog.mapper.WhileListMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_while_list】的数据库操作Service实现
* @createDate 2024-08-04 18:18:41
*/
@Service
public class WhileListServiceImpl extends ServiceImpl<WhileListMapper, WhileList>
    implements WhileListService{

    @Autowired
    private WhileListMapper whileListMapper;

    @Override
    public void saveBlackList(String realIp) {
        WhileList whileList = new WhileList();
        whileList.setIp(realIp);
        whileList.setStatus(WhileList.Status.FORBIDDEN);
        this.save(whileList);
    }

    @Override
    public PageInfo<WhileList> pageWhiteList(WhiteListQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        PageHelper.orderBy("create_time desc");
        List<WhileList> sysLogList = whileListMapper.pageWhiteList(query);
        return new PageInfo<>(sysLogList);
    }
}




