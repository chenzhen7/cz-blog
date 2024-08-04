package com.chenzhen.blog.entity.query;

import lombok.Data;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/8/4 21:09
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class WhiteListQuery extends BaseQuery {


    private String ip;

    private Integer status;
}
