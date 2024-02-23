package com.chenzhen.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenzhen.blog.entity.pojo.Music;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【t_music】的数据库操作Mapper
* @createDate 2024-02-23 16:38:59
* @Entity com.chenzhen.blog.entity.pojo.Music
*/
@Mapper
public interface MusicMapper extends BaseMapper<Music> {

}




