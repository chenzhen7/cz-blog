package com.chenzhen.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.SysLog;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.entity.vo.BlogVO;
import com.chenzhen.blog.service.SysLogService;
import com.chenzhen.blog.mapper.SysLogMapper;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author MIS
* @description 针对表【t_sys_log】的数据库操作Service实现
* @createDate 2024-07-27 19:09:52
*/
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog>
    implements SysLogService{

    @Autowired
    private Map<String, String> spiderMap;
    @Autowired
    private SysLogMapper sysLogMapper;


    @Override
    @Async("asyncThreadPoolTaskExecutor")
    public void asyncSaveSystemLog(String content,HttpServletRequest request,String realIp) {
        String ua = request.getHeader("User-Agent");

        SysLog sysLog = new SysLog();
        sysLog.setContent(content);
        sysLog.setIp(realIp);
        sysLog.setReferer(request.getHeader("Referer"));
        sysLog.setRequestUrl(request.getRequestURL().toString());
        sysLog.setSpiderType(parseUa(ua));
        sysLog.setParams(request.getQueryString());

        try {
            UserAgent agent = UserAgentUtil.parse(ua);
            sysLog.setBrowser(agent.getBrowser().getName());
            sysLog.setOs(agent.getOs().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        save(sysLog);

    }

    @Override
    public PageInfo<SysLog> pageSysLogs(BaseQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        PageHelper.orderBy("create_time desc");
        List<SysLog> sysLogList = sysLogMapper.pageAdminBlogs(query);
        return new PageInfo<>(sysLogList);
    }





    private String parseUa(String ua) {
        if (StringUtils.isEmpty(ua)) {
            return null;
        }
        Map<String, String> spider = spiderMap;
        for (Map.Entry<String, String> entry : spider.entrySet()) {
            String spiderSign = entry.getKey();// 爬虫的标记
            if (ua.contains(spiderSign) || ua.equalsIgnoreCase(spiderSign) || ua.toLowerCase().contains(spiderSign.toLowerCase())) {
                return entry.getValue();
            }
        }
        return null;
    }

}




