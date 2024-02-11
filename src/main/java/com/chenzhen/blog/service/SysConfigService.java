package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.enums.SysConfigEnum;
import com.chenzhen.blog.entity.pojo.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.github.pagehelper.PageInfo;

/**
* @author Administrator
* @description 针对表【t_sys_config(系统配置表)】的数据库操作Service
* @createDate 2024-01-20 12:11:33
*/
public interface SysConfigService extends IService<SysConfig> {

    PageInfo<SysConfig> pageSysConfigs(BaseQuery query);

    SysConfig getByEnums(SysConfigEnum configEnum);

}
