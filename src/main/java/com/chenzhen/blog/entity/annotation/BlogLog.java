package com.chenzhen.blog.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/27 15:59
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BlogLog {
    /**
     * 业务的名称
     */
    String value() default "";

    /**
     * 是否将当前日志记录到数据库中
     */
    boolean save() default true;
}
