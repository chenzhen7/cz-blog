package com.chenzhen.blog.entity.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.chenzhen.blog.entity.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author MIS
* @description 针对表【t_user】的数据库操作Mapper
* @createDate 2022-09-11 18:21:11
* @Entity com.chenzhen.blog.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectOneByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}




