package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.Tag;
import com.chenzhen.blog.entity.vo.TagVO;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.TagService;
import com.chenzhen.blog.mapper.TagMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_tag】的数据库操作Service实现
* @createDate 2024-01-11 14:54:30
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public PageInfo<Tag> pageTags(BaseQuery query) {
        PageHelper.startPage(query.getPageNum(), 10);
        PageHelper.orderBy("create_time desc");

        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();

        List<Tag> list = list(wrapper);

        return new PageInfo<>(list);

    }

    @Override
    public List<TagVO> listTagsVO() {
        return baseMapper.listTagsVO();
    }
}




