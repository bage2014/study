package com.bage.study.ai.best.practice.dev.flow.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackPointRequest {

    @NotNull(message = "纬度不能为空")
    @DecimalMin(value = "-90", message = "纬度必须在 -90 到 90 之间")
    @DecimalMax(value = "90", message = "纬度必须在 -90 到 90 之间")
    private Double latitude;

    @NotNull(message = "经度不能为空")
    @DecimalMin(value = "-180", message = "经度必须在 -180 到 180 之间")
    @DecimalMax(value = "180", message = "经度必须在 -180 到 180 之间")
    private Double longitude;

    @Size(max = 100, message = "名称长度不能超过100个字符")
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;
}