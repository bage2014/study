package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MemberDTO {

    private Long id;
    private Long familyId;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String photo;
    private String details;
    private String phone;
    private String email;
    private String occupation;
    private String education;
    private LocalDateTime createdAt;
}