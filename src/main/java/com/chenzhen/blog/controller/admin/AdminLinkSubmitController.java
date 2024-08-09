package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.dto.BaiduLinkSubmitDTO;
import com.chenzhen.blog.entity.dto.SyncCsdnDTO;
import com.chenzhen.blog.service.LinkSubmitService;
import com.chenzhen.blog.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/8/10 0:18
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@CrossOrigin
@Controller
@RequestMapping("/admin/baidu")
public class AdminLinkSubmitController {

    @Autowired
    private LinkSubmitService linkSubmitService;

    /**
     * 批量推送百度接口
     */
    @ResponseBody
    @PostMapping("/batchLinkSubmit")
    public R batchLinkSubmit(@RequestBody BaiduLinkSubmitDTO dto){
        return linkSubmitService.batchLinkSubmit(dto);
    }
}
