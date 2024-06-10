package com.chenzhen.blog;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync//开启异步
//@EnableCaching//开启基于缓存的注解
//在主应用程序类上使用@EnableConfigurationProperties注解来启用配置属性：
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  博客项目启动成功   ლ(´ڡ`ლ)ﾞ");
    }


}
