package com.chenzhen.blog.mapper;

import com.chenzhen.blog.entity.pojo.WhileList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenzhen.blog.entity.query.WhiteListQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_while_list】的数据库操作Mapper
* @createDate 2024-08-04 18:18:41
* @Entity com.chenzhen.blog.entity.pojo.WhileList
*/
@Mapper
public interface WhileListMapper extends BaseMapper<WhileList> {

    List<WhileList> pageWhiteList(WhiteListQuery query);
}




