package com.chenzhen.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.pojo.Music;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.github.pagehelper.PageInfo;

/**
* @author Administrator
* @description 针对表【t_music】的数据库操作Service
* @createDate 2024-02-23 16:38:59
*/
public interface MusicService extends IService<Music> {

    PageInfo<Music> pageMusic(BaseQuery query);
}
