package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.enums.SysConfigEnum;
import com.chenzhen.blog.entity.pojo.SysConfig;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.mapper.SysConfigMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_sys_config(系统配置表)】的数据库操作Service实现
* @createDate 2024-01-20 12:11:33
*/
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
    implements SysConfigService{

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public PageInfo<SysConfig> pageSysConfigs(BaseQuery query) {
        PageHelper.startPage(query.getPageNum(), 10);
        PageHelper.orderBy("create_time desc");

        List<SysConfig> list = list();

        return new PageInfo<>(list);
    }

    @Override
    public SysConfig getByEnums(SysConfigEnum configEnum) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getName, configEnum.getName());
        return sysConfigMapper.selectOne(wrapper);
    }
}




