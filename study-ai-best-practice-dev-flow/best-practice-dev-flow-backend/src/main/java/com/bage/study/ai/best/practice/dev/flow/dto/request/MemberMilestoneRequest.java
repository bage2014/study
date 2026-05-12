package com.bage.study.ai.best.practice.dev.flow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberMilestoneRequest {

    @NotNull(message = "成员ID不能为空")
    private Long memberId;

    @NotBlank(message = "标题不能为空")
    @Size(min = 1, max = 200, message = "标题长度必须在1-200个字符之间")
    private String title;

    private String content;
}