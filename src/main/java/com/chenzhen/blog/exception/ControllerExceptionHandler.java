package com.chenzhen.blog.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.chenzhen.blog.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/11 15:36
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    //    将异常记录到日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {

        //记录异常信息：请求的URL，异常信息
        logger.error("Requst URL : {}，Exception : {}", request.getRequestURL(),e);

        //当标识了状态码的时候就不拦截
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        //将记录的异常信息返回到error页面
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("message", e.getMessage());
        mv.setViewName("error/error");
        return mv;
    }

    @ExceptionHandler(BlogException.class)
    public ModelAndView blogExceptionHandler(HttpServletRequest request, BlogException e) throws Exception {

        //记录异常信息：请求的URL，异常信息
        logger.error("Requst URL : {}，Exception : {}", request.getRequestURL(),e.getMsg());

        //将记录的异常信息返回到error页面
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("message", e.getMsg());
        mv.setViewName("error/error");
        return mv;
    }

    @ExceptionHandler(AdminException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R AdminExceptionHandler(HttpServletRequest request, AdminException e){

        logger.error("请求URL : {}，异常 : {}", request.getRequestURL(),e.getMsg());

        return R.error(e.getMsg());
    }

    @ExceptionHandler(NotLoginException.class)
    public ModelAndView NotLoginExceptionHandler(NotLoginException nle) {

        // 判断场景值，定制化异常信息
        String message;
        if(nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "用户未登录";
        }
        else if(nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "用户信息无效或已过期，请重新登录";
        }
        else if(nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "用户信息无效或已过期，请重新登录";
        }
        else if(nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "用户信息无效或已过期，请重新登录";
        }
        else if(nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "用户信息无效或已过期，请重新登录";
        }
        else {
            message = "用户未登录";
        }

        logger.warn("鉴权异常:{}",message);

        //将记录的异常信息返回到error页面
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", message);
        mv.setViewName("error/error");

        return mv;

    }


    @ExceptionHandler(NotRoleException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R NotLoginExceptionHandler(NotRoleException npe){
        // 判断场景值，定制化异常信息
        String message = "无权操作！";

        logger.warn("无权限异常:{}",npe.getMessage());

        return R.error(message);
    }


    /**
     * 处理方法参数校验失败的异常，例如使用校验注解（如@Valid）时的校验失败。
     * 当校验失败时，返回一个带有错误信息的正常响应，HTTP状态码为200 OK。
     *
     * @param e MethodArgumentNotValidException异常对象，包含校验失败的详细信息。
     * @return 包含错误信息的响应对象。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        // 获取校验结果信息
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = String.format("%s",fieldError.getDefaultMessage());
        logger.warn("参数校验出错:{}",message);
        // 返回包含错误信息的响应
        return R.error(message);
    }
}