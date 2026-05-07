package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedMember {
    private Long id;
    private String name;
    private String gender;
    private Integer birthYear;
    private String position;
    private Double confidence;
    private boolean exists;
    private Long existingId;
}