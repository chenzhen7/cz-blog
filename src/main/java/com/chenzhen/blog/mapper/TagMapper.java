package com.chenzhen.blog.mapper;

import com.chenzhen.blog.entity.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenzhen.blog.entity.vo.TagVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_tag】的数据库操作Mapper
* @createDate 2024-01-11 14:54:30
* @Entity com.chenzhen.blog.pojo.Tag
*/
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<TagVO> listTagsVO();
}




