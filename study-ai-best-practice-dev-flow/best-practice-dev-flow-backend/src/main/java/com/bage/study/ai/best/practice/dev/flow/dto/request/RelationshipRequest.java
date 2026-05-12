package com.bage.study.ai.best.practice.dev.flow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelationshipRequest {

    @NotNull(message = "成员ID1不能为空")
    private Long memberId1;

    @NotNull(message = "成员ID2不能为空")
    private Long memberId2;

    @NotBlank(message = "关系类型不能为空")
    private String relationType;

    private String description;
}