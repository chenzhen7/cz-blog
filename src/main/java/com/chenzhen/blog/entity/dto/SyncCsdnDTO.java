package com.chenzhen.blog.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/21 18:15
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Data
public class SyncCsdnDTO {

    private List<Integer> ids;

    private Long typeId;
}
