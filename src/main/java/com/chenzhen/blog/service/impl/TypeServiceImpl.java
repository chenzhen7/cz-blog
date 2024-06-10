package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.Type;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.TypeService;
import com.chenzhen.blog.mapper.TypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 15832
* @description 针对表【t_type】的数据库操作Service实现
* @createDate 2024-05-28 11:12:27
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

    @Override
    public PageInfo<Type> pageTypes(BaseQuery query) {
        PageHelper.startPage(query.getPageNum(), 10);
        PageHelper.orderBy("create_time desc");

        return new PageInfo<>(list());

    }

    @Override
    public List<Type> getTypeList() {
        return list(new QueryWrapper<Type>().orderByDesc("create_time"));
    }
}




