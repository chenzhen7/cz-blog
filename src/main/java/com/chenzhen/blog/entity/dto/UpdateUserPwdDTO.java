package com.chenzhen.blog.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateUserPwdDTO {

    private Long id;

    @Size(min = 6, max = 20, message = "密码长度必须在6到20个字符之间")
    @NotBlank
    private String oldPassword;

    @Size(min = 6, max = 20, message = "密码长度必须在6到20个字符之间")
    @NotBlank
    private String newPassword;

}
