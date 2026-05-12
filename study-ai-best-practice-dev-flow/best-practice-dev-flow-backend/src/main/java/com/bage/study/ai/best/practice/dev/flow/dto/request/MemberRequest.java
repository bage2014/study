package com.bage.study.ai.best.practice.dev.flow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberRequest {

    @NotNull(message = "家族ID不能为空")
    private Long familyId;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 64, message = "姓名长度必须在1-64个字符之间")
    private String name;

    private String gender;

    private LocalDate birthDate;

    private LocalDate deathDate;

    private String photo;

    private String details;

    private String phone;

    private String email;
}