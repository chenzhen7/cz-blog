package com.chenzhen.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.dto.LoginDTO;
import com.chenzhen.blog.entity.dto.UpdateUserPwdDTO;
import com.chenzhen.blog.entity.pojo.User;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.UserService;
import com.chenzhen.blog.mapper.UserMapper;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author MIS
 * @description 针对表【t_user】的数据库操作Service实现
 * @createDate 2022-09-11 18:21:11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{

    @Override
    public User checkUser(String username, String password) {
//        MD5加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());

        User user = getBaseMapper().selectOneByUsernameAndPassword(username, md5DigestAsHex);

        return user;
    }

    @Override
    public R login(LoginDTO loginDTO, HttpSession session) {
        User user = checkUser(loginDTO.getUsername(), loginDTO.getPassword());
        if (user == null) {
            return R.error("用户名或密码错误");
        }
        // 登录 设置永不过期
        StpUtil.login(user.getId(), true);
        //密码置空
        user.setPassword(null);
        // 保存用户
        session.setAttribute("user",user);
        // 设置永不过期
        session.setMaxInactiveInterval(0);
        // 第3步，返回token给前端
        return R.success();

    }

    @Override
    public PageInfo<User> pageUsers(BaseQuery query) {
        PageHelper.startPage(query.getPageNum(), 10);
        PageHelper.orderBy("create_time desc");

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        List<User> list = list(wrapper);

        return new PageInfo<>(list);
    }

    @Override
    public R updatePassword(UpdateUserPwdDTO updateUserPwdDTO) {
        User user = getById(updateUserPwdDTO.getId());
        if (user == null) {
            return R.error("用户不存在");
        }
        //对比用户输入的旧密码和数据库的是否一致
        String oldPassword_Md5 = DigestUtils.md5DigestAsHex(updateUserPwdDTO.getOldPassword().getBytes());
        if (!user.getPassword().equals(oldPassword_Md5)) {
            return R.error("旧密码错误");
        }
        String newPassword_Md5 = DigestUtils.md5DigestAsHex(updateUserPwdDTO.getNewPassword().getBytes());

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .set(User::getPassword, newPassword_Md5)
                .eq(User::getId, updateUserPwdDTO.getId());

        return update(wrapper) ? R.success() : R.error("修改密码失败");
    }
}




