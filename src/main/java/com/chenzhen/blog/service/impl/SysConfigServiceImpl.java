package com.chenzhen.blog.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.enums.SysConfigEnum;
import com.chenzhen.blog.entity.pojo.SysConfig;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.vo.SkillVO;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.mapper.SysConfigMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Override
    public List<SkillVO> getSkills() {
        //获取技能字符串
        String skillStr = getByEnums(SysConfigEnum.ABOUT_ME_SKILL).getValue();
        //逗号分割
        String[] skills = skillStr.split("[,，]");

        List<SkillVO> skillList = new ArrayList<>();
        for (int i = 0; i < skills.length; i++) {
            // 类名为宽度-3，每行12个宽度能够显示4个元素
            String currentClass = "col-" + getRandomCol();
            skillList.add(new SkillVO(skills[i], currentClass));
        }

        return skillList;
    }

    public static int getRandomCol() {
        //宽度
        int[] numbers = {3, 4, 6, 12};
        //概率
        int[] weights = {40, 30, 20, 10};

        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }

        Random random = new Random();
        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (randomValue < cumulativeWeight) {
                return numbers[i];
            }
        }
        return numbers[0];
    }
}




