package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.vo.TagVO;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_tag】的数据库操作Service
* @createDate 2024-01-11 14:54:30
*/
public interface TagService extends IService<Tag> {

    PageInfo<Tag> pageTags(BaseQuery query);

    List<TagVO> listTagsVO();

}
