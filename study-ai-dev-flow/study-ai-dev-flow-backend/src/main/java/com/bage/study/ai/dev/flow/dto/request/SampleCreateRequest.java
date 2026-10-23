package com.bage.study.ai.dev.flow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SampleCreateRequest {

    @NotBlank(message = "名称不能为空")
    private String name;

    private String description;
}
