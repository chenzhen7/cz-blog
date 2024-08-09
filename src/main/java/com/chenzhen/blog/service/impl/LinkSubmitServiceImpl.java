package com.chenzhen.blog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.chenzhen.blog.entity.dto.BaiduLinkSubmitDTO;
import com.chenzhen.blog.entity.vo.SysConfigVO;
import com.chenzhen.blog.exception.AdminException;
import com.chenzhen.blog.sdk.CommonResult;
import com.chenzhen.blog.sdk.baidu.LinkSubmitClient;
import com.chenzhen.blog.sdk.baidu.LinkSubmitRequest;
import com.chenzhen.blog.sdk.baidu.LinkSubmitResp;
import com.chenzhen.blog.service.LinkSubmitService;
import com.chenzhen.blog.service.SysConfigService;
import com.chenzhen.blog.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/8/10 0:21
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Service
@Slf4j
public class LinkSubmitServiceImpl implements LinkSubmitService {

    @Autowired
    private SysConfigService sysConfigService;
    @Override
    public R batchLinkSubmit(BaiduLinkSubmitDTO dto) {
        log.info("批量提交友链：{}", dto);
        SysConfigVO sysConfig = sysConfigService.getSysConfig();

        if (StrUtil.isBlank(sysConfig.getBaiduLinkSubmitSite())){
            throw new AdminException("请先配置在百度搜索资源平台验证的站点");
        }
        if (StrUtil.isBlank(sysConfig.getBaiduLinkSubmitToken())){
            throw new AdminException("请先配置在百度搜索资源平台验证的token");
        }

        LinkSubmitRequest submitRequest = LinkSubmitRequest.builder()
                .site(sysConfig.getBaiduLinkSubmitSite())
                .token(sysConfig.getBaiduLinkSubmitToken())
                .urls(dto.getIds().stream().map(id -> "https://www.chenzhen.space/blog/" + id).collect(Collectors.toList()))
                .build();

        CommonResult<LinkSubmitResp> commonResult = LinkSubmitClient.linkSubmit(submitRequest);
        if (commonResult.getSuccess()){
            LinkSubmitResp submitResp = commonResult.getResult();
            log.info("批量提交友链成功：{}", submitResp);
            return R.success().data("submitResp",submitResp);
        }
        log.error("批量提交友链失败：{}", commonResult.getMessage());
        return R.error(commonResult.getMessage());
    }
}
