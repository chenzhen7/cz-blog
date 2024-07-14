package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.vo.SysConfigVO;

/**
* @author MIS
* @description 针对表【t_sys_config(系统配置表)】的数据库操作Service
* @createDate 2024-07-11 21:54:19
*/
public interface SysConfigService extends IService<SysConfig> {

    SysConfigVO getSysConfig();

    boolean updateSysConfig(SysConfigVO sysConfig);
}
