package com.bage.study.ai.best.practice.dev.flow.dto.request;

import lombok.Data;

@Data
public class UserProfileRequest {

    private String nickname;

    private String avatar;

    private String phone;
}