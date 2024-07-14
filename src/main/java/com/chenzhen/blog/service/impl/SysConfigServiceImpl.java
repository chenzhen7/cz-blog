package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.SysConfig;
import com.chenzhen.blog.entity.vo.SysConfigVO;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.entity.mapper.SysConfigMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author MIS
* @description 针对表【t_sys_config(系统配置表)】的数据库操作Service实现
* @createDate 2024-07-11 21:54:19
*/
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
    implements SysConfigService{

    @Override
    public SysConfigVO getSysConfig() {
        SysConfig sysConfig = list().get(0);
        SysConfigVO sysConfigVO = new SysConfigVO();
        BeanUtils.copyProperties(sysConfig,sysConfigVO);
        // 将字符串转为数组
        sysConfigVO.setAboutMeSkills(sysConfig.getAboutMeSkill().split(","));
        return sysConfigVO;
    }

    @Override
    public boolean updateSysConfig(SysConfigVO sysConfigVO) {
        SysConfig sysConfig = new SysConfig();
        BeanUtils.copyProperties(sysConfigVO,sysConfig);
        sysConfig.setAboutMeSkill(String.join(",",sysConfigVO.getAboutMeSkills()));
        return updateById(sysConfig);
    }

}




