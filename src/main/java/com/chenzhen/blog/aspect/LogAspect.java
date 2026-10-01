package com.chenzhen.blog.aspect;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chenzhen.blog.entity.annotation.BlogLog;
import com.chenzhen.blog.entity.pojo.WhiteList;
import com.chenzhen.blog.exception.BlogException;
import com.chenzhen.blog.service.SysLogService;
import com.chenzhen.blog.service.WhiteListService;
import com.chenzhen.blog.util.MemoryDBUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    private WhiteListService whiteListService;
    @Autowired
    private MemoryDBUtil<Long> memoryDBUtil;
    //时间段（单位：秒）
    private static final long time = 60;
    //时间段内最大访问次数
    private static final int maxCount = 60;
    /* 取真实 IP 时按顺序看的转发头。
       isLocalRequest 判断「有没有经过代理」也用这一份 —— 名单各写一遍，迟早只改一处。 */
    private static final String[] IP_HEADERS = {
            "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
            "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
    };

    // 获取日志信息
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String IPV4_IPV6_PATTERN = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|(((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|:)))(%.+)?)";

    //定义切面，申明log()是一个切面
    @Pointcut("@annotation(com.chenzhen.blog.entity.annotation.BlogLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取ip
        String realIp = getRealIp(request);
        //判断是否黑名单。本机访问自己不进这一步，理由见 isLocalRequest
        if (!isLocalRequest(request)) {
            before(realIp);
        }
        //执行业务
        Object result = point.proceed();
        try {
            handle(point,realIp,request);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    private void before(String realIp) {
        Set<String> blackList = whiteListService.getBlackList();
        //先判断是否在内存的黑名单中，如果在，则直接抛出异常，不再访问数据库
        if (blackList.contains(realIp)){
            log.info("{}在黑名单中，访问被拒绝!",realIp);
            throw new BlogException("访问被拒绝!");
        }
        synchronized (this){
            //访问量+1
            Long currentCount = memoryDBUtil.increment(realIp,1, time);
            //超过最大访问次数
            if (currentCount > maxCount) {
                log.info("{}访问次数超过{}次，加入黑名单",realIp,maxCount);
                List<WhiteList> whiteLists = whiteListService.list(new LambdaQueryWrapper<WhiteList>().eq(WhiteList::getIp, realIp));
                //如果数据库中没有该ip，则添加到数据库中黑名单
                if (CollUtil.isEmpty(whiteLists)){
                    whiteListService.saveBlackList(realIp);
                }
                //随后加入内存黑名单
                blackList.add(realIp);
                throw new BlogException("访问被拒绝!");

            }
        }
    }

    private void handle(ProceedingJoinPoint point,String realIp,HttpServletRequest request) throws Exception {
        //控制台打印日志
        printLog(point, request);
        //获取注解
        BlogLog annotation = getAnnotation(point);
        //是否保存日志
        boolean save = annotation.save();
        //业务名称
        String content = annotation.value();
        if (save) {
            logService.asyncSaveSystemLog(content,request,realIp);
        }


    }

    private BlogLog getAnnotation(ProceedingJoinPoint point) throws NoSuchMethodException {
        MethodSignature msig = (MethodSignature) point.getSignature();
        //获取方法对象
        Method currentMethod = point.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解对象
        return currentMethod.getAnnotation(BlogLog.class);
    }

    private void printLog(ProceedingJoinPoint point,HttpServletRequest request) {

        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = point.getSignature().getName();
        String params = JSONUtil.toJsonStr(request.getParameterMap());

        logger.info("访问地址: {}, 访问者IP: {}, 访问方法: {} ，参数: {}", url, ip, classMethod, params);
    }

    /**
     * 获取当前请求者的ip
     *
     * @return {String}
     */
    public String getRealIp(HttpServletRequest request) {
        try {
            if (null == request) {
                return "";
            }
            String ip;
            for (String header : IP_HEADERS) {
                ip = request.getHeader(header);
                if (isValidIp(ip)) {
                    return getMultistageReverseProxyIp(ip);
                }
            }
            ip = request.getRemoteAddr();
            return getMultistageReverseProxyIp(ip);
        } catch (Exception e) {
            log.error("获取ip出错!");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断这次请求是不是「本机访问自己」。
     *
     * 它是一个有意为之的例外：本机调试（浏览器开 127.0.0.1、curl、Playwright 跑回归）
     * 一分钟内很容易把 60 次的额度打满，接着这个 IP 被**永久**写进黑名单 ——
     * 结果是开发时最常访问的那个人，第一个被关在门外。回环地址代表的是
     * 「跑着这个服务的这台机器本身」，不该受这套针对外部访客的限流管。
     *
     * 两个条件缺一不可：
     *   一、请求没有带任何转发头。带了就说明前面站着反向代理，这段回环连接是
     *       「代理转发到应用」而不是「本机自己访问自己」，真实访客另有其人
     *       （就是头里那个 IP）；放行等于把限流整个关掉。
     *   二、getRemoteAddr() 落在回环地址上（IPv4 / IPv6 都认）。
     * 刻意**不**看 X-Forwarded-For 里的值来判断本机：那个头客户端随便写，
     * 拿它认本机，就是给限流开一个「把自己说成本机」的后门。
     *
     * @param request 当前请求
     * @return true 表示本机直连，调用方应跳过黑名单与频率统计
     */
    private boolean isLocalRequest(HttpServletRequest request) {
        if (null == request) {
            return false;
        }
        for (String header : IP_HEADERS) {
            if (isValidIp(request.getHeader(header))) {
                return false;
            }
        }
        String remoteAddr = request.getRemoteAddr();
        try {
            return InetAddress.getByName(remoteAddr).isLoopbackAddress();
        } catch (UnknownHostException e) {
            // 解析不出来就按外部访客处理：保守的一侧是「继续限流」，不是「放行」。
            // 但要留下日志 —— 这条路静默走过一次，就再也查不出某段流量为什么没被限住。
            log.warn("请求来源地址无法解析，按外部访客处理：{}", remoteAddr, e);
            return false;
        }
    }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    private String getMultistageReverseProxyIp(String ip) {
        // 多级反向代理检测
        if (ip != null && ip.indexOf(",") > 0) {
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (isValidIp(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ReUtil.getGroup0(IPV4_IPV6_PATTERN, ip);
    }


    /**
     * 校验IP
     *
     * @param ip 获得的IP地址
     * @return 是否为未知的ip
     */
    private boolean isValidIp(String ip) {
        return !StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip) && isIp(ip);
    }

    private boolean isIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        String regex = "^\\s*(((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4})|:))|(([0-9A-Fa-f]{1,4}:){6}(:|((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})|(:[0-9A-Fa-f]{1,4})))|(([0-9A-Fa-f]{1,4}:){5}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:)(:[0-9A-Fa-f]{1,4}){0,4}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(:(:[0-9A-Fa-f]{1,4}){0,5}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})))\\;?\\s*)*$";
        return checkByRegex(ip, regex);
    }

    private boolean checkByRegex(String str, String regex) {
        if (null == str) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

}