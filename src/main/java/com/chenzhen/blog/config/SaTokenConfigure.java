package com.chenzhen.blog.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/12/23 18:37
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                // 拦截 /admin/** 路径下的所有请求用于检查登录状态
                .addPathPatterns("/admin/**")
                // 排除 /admin 和 /admin/login 路径
                .excludePathPatterns("/admin","/admin/login");
    }
}
