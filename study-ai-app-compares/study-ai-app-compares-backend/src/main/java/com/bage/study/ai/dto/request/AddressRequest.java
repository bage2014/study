package com.bage.study.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    @NotBlank(message = "手机号码不能为空")
    private String phoneNumber;

    @NotBlank(message = "省市区不能为空")
    private String provinceCityDistrict;

    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;

    private Boolean isDefault;
}