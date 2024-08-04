package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.SysLogService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/8/4 17:54
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Controller
@CrossOrigin
@RequestMapping("/admin/sysLogs")
public class AdminSysLogController {

    @Autowired
    SysLogService sysLogService;

    @GetMapping("")
    public String dashboard() {
        return "admin/sys-logs";
    }

    @GetMapping("/page")
    @ResponseBody
    public R pageSysLogs(BaseQuery query) {
        return R.success().data("page",sysLogService.pageSysLogs(query));
    }


}
