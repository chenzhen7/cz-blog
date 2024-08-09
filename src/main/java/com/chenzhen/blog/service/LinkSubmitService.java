package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.dto.BaiduLinkSubmitDTO;
import com.chenzhen.blog.util.R;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/8/10 0:21
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public interface LinkSubmitService {

    R batchLinkSubmit(BaiduLinkSubmitDTO dto);
}
