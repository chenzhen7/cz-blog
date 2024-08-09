package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.WhiteList;
import com.chenzhen.blog.entity.query.WhiteListQuery;
import com.chenzhen.blog.service.WhiteListService;
import com.chenzhen.blog.mapper.WhiteListMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author MIS
* @description 针对表【t_while_list】的数据库操作Service实现
* @createDate 2024-08-04 18:18:41
*/
@Service
public class WhiteListServiceImpl extends ServiceImpl<WhiteListMapper, WhiteList>
    implements WhiteListService {

    @Autowired
    private WhiteListMapper whileListMapper;

    private Set<String> blackList;

    @Override
    public void saveBlackList(String realIp) {
        WhiteList whiteList = new WhiteList();
        whiteList.setIp(realIp);
        whiteList.setStatus(WhiteList.Status.FORBIDDEN);
        this.save(whiteList);
    }

    @Override
    public PageInfo<WhiteList> pageWhiteList(WhiteListQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        PageHelper.orderBy("create_time desc");
        List<WhiteList> sysLogList = whileListMapper.pageWhiteList(query);
        return new PageInfo<>(sysLogList);
    }

    @PostConstruct
    public void initBlackList() {
        Set<String> blackList = list().stream().map(WhiteList::getIp).collect(Collectors.toSet());
        this.blackList = blackList;
    }

    public Set<String> getBlackList() {
        return blackList;
    }
}




