package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.dto.LoginDTO;
import com.chenzhen.blog.entity.dto.UpdateUserPwdDTO;
import com.chenzhen.blog.entity.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpSession;

/**
* @author MIS
* @description 针对表【t_user】的数据库操作Service
* @createDate 2022-09-11 18:21:11
*/
public interface UserService extends IService<User> {
    /**
     * 根据username和password查找用户
     */
    User checkUser(String username,String password);

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    R login(LoginDTO loginDTO, HttpSession session);

    PageInfo<User> pageUsers(BaseQuery query);

    R updatePassword(UpdateUserPwdDTO updateUserPwdDTO);

}
