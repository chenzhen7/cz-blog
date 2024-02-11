package com.chenzhen.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenzhen.blog.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User extends BaseEntity implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    //邮箱
    private String email;
    //头像
    private String avatar;
    //昵称
    private String nickname;
    //用户名
    private String username;
    //密码
    private String password;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}