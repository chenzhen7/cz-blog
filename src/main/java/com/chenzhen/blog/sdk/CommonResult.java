package com.chenzhen.blog.sdk;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/12/21 22:08
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 共同响应参数
 *
 * @author EDaoren
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
    /**
     * 状态码（true;false）
     */
    private Boolean success;

    /**
     * 异常代码KEY
     */
    private String code;

    /**
     * 异常信息提示，（成功时是“ ”）
     */
    private String message;

    /**
     * 具体结果
     */
    private T result;
}