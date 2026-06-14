package com.bage.study.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private String id;

    private String receiverName;

    private String phoneNumber;

    private String provinceCityDistrict;

    private String detailAddress;

    private Boolean isDefault;
}