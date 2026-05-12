package com.bage.study.ai.best.practice.dev.flow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HistoricalEventRequest {

    @NotNull(message = "家族ID不能为空")
    private Long familyId;

    @NotBlank(message = "事件名称不能为空")
    @Size(min = 1, max = 200, message = "事件名称长度必须在1-200个字符之间")
    private String name;

    @Size(max = 2000, message = "事件描述长度不能超过2000个字符")
    private String description;

    private LocalDate eventDate;

    private String relatedMemberIds;

    private String photo;
}