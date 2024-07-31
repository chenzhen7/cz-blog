package com.chenzhen.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.pojo.SysLog;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageInfo;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
* @author MIS
* @description 针对表【t_sys_log】的数据库操作Service
* @createDate 2024-07-27 19:09:52
*/
public interface SysLogService extends IService<SysLog> {

    void asyncSaveSystemLog(String content,ServletRequestAttributes attributes);

    PageInfo<SysLog> pageSysLogs(BaseQuery query);
}
