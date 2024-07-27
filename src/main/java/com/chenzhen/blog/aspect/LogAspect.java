package com.chenzhen.blog.aspect;


import cn.hutool.json.JSONUtil;
import com.chenzhen.blog.entity.annotation.BlogLog;
import com.chenzhen.blog.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/11 15:08
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    private SysLogService logService;
    // 获取日志信息
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //定义切面，申明log()是一个切面
    @Pointcut("@annotation(com.chenzhen.blog.entity.annotation.BlogLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object writeLog(ProceedingJoinPoint point) throws Throwable {

        //先执行业务
        Object result = point.proceed();

        try {
            handle(point);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    private void handle(ProceedingJoinPoint point) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = point.getSignature().getName();
        String params = JSONUtil.toJsonStr(request.getParameterMap());

        logger.info("访问地址: {}, 访问者IP: {}, 访问方法: {} ，参数: {}", url, ip, classMethod, params);

        Signature sig = point.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Method currentMethod = point.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取操作名称
        BlogLog annotation = currentMethod.getAnnotation(BlogLog.class);
        boolean save = annotation.save();
        String content = annotation.value();
        if (!save) {
            return;
        }
        logService.asyncSaveSystemLog(content,attributes);
    }


}