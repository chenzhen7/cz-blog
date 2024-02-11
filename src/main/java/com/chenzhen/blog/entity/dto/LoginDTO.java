package com.chenzhen.blog.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/1/13 16:33
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class LoginDTO {

    @NotBlank(message = "账号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20个字符之间")
    private String password;
}
