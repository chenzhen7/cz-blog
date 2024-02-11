package com.chenzhen.blog.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/11 15:08
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Aspect
@Component
public class LogAspect {
    // 获取日志信息
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //定义切面，申明log()是一个切面
    @Pointcut("execution(* com.chenzhen.blog.controller..*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();

        logger.info("访问地址: {}, 访问者IP: {}, 访问方法: {} ，参数: {}", url, ip, classMethod, args);

        // 将开始时间存储在请求属性中，以便后续计算耗时
        request.setAttribute("startTime", startTime);
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        // 计算请求耗时
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        logger.info("结果: {} , 请求耗时: {} 毫秒", result.getClass().getName(),elapsedTime);
    }


}