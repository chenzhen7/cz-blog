package com.chenzhen.blog.sdk.baidu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/12/21 22:08
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkSubmitResp implements Serializable {

    /**
     * 成功推送的url条数
     */
    private Integer success;
    /**
     * 当天剩余的可推送url条数
     */
    private Integer remain;
    /**
     * 由于不是本站url而未处理的url列表
     */
    private List<String> not_same_site;
    /**
     * 不合法的url列表
     */
    private List<String> not_valid;


}