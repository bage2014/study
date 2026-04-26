package com.familytree.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FamilyCreateRequest {

    @NotBlank(message = "家族名称不能为空")
    @Size(min = 1, max = 100, message = "家族名称长度必须在1-100个字符之间")
    private String name;

    @Size(max = 2000, message = "家族描述长度不能超过2000个字符")
    private String description;

    private String avatar;
}