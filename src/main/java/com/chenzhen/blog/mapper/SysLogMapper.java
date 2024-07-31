package com.chenzhen.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenzhen.blog.entity.pojo.SysLog;
import com.chenzhen.blog.entity.query.BaseQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_sys_log】的数据库操作Mapper
* @createDate 2024-07-27 19:09:52
* @Entity com.chenzhen.blog.pojo.SysLog
*/
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    List<SysLog> pageAdminBlogs(BaseQuery query);
}




