package com.chenzhen.blog.entity.dto;

import lombok.Data;

/**
 * 友链审核dto
 */
@Data
public class FriendlinkAuditDTO {
    // 友链id
    private Long id;
    // 拒绝原因
    private String reason;
}
