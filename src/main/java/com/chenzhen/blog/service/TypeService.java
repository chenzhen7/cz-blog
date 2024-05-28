package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.vo.TypeBlogVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* @author 15832
* @description 针对表【t_type】的数据库操作Service
* @createDate 2024-05-28 11:12:27
*/
public interface TypeService extends IService<Type> {

    PageInfo<Type> pageTypes(BaseQuery query);

    List<Type> getTypeList();
}
