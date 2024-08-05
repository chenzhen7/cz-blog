package com.chenzhen.blog.mapper;

import com.chenzhen.blog.entity.pojo.WhiteList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenzhen.blog.entity.query.WhiteListQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_White_list】的数据库操作Mapper
* @createDate 2024-08-04 18:18:41
* @Entity com.chenzhen.blog.entity.pojo.WhiteList
*/
@Mapper
public interface WhiteListMapper extends BaseMapper<WhiteList> {

    List<WhiteList> pageWhiteList(WhiteListQuery query);
}




