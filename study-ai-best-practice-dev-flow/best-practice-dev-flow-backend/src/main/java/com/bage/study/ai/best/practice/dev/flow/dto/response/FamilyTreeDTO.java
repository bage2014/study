package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class FamilyTreeDTO {

    private Long memberId;
    private String name;
    private String gender;
    private List<FamilyTreeDTO> children;
}