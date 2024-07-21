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

    private Integer page;

    private Integer pageSize;

    /**
     * 状态
     * @see Status
     */
    private String status;

    /**
     * 关键词
     */
    private String keyword;


    public interface Status{
        /**
         * 全部
         */
        String ALL = "all";
        /**
         * 全部可见
         */
        String ENABLE = "enable";

        /**
         * 仅我可见
         */
        String PRIVATE = "private";

        /**
         * 审核
         */
        String AUDIT = "audit";
        /**
         * 草稿箱
         */
        String DRAFT = "draft";
        /**
         * 回收站
         */
        String DELETED = "deleted";


    }

}
