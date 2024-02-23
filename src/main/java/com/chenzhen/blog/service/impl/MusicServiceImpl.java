package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.Music;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.mapper.MusicMapper;
import com.chenzhen.blog.service.MusicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_music】的数据库操作Service实现
* @createDate 2024-02-23 16:38:59
*/
@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music>
    implements MusicService{

    @Override
    public PageInfo<Music> pageMusic(BaseQuery query) {
        PageHelper.startPage(query.getPageNum(), 10);
        PageHelper.orderBy("create_time desc");
        List<Music> list = list();
        return new PageInfo<>(list);
    }
}




