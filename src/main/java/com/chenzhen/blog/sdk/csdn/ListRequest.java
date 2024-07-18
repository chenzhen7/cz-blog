package com.chenzhen.blog.sdk.csdn;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author chenjixian
 * @date 2024/7/18 10:25
 * @description:
 */
@SuperBuilder
@Data
public class ListRequest extends BizRequest {

    private Integer pageSize;

}
