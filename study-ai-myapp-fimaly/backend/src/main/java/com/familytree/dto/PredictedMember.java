package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictedMember {
    private Long id;
    private String name;
    private Integer birthYear;
    private String gender;
    private String nativePlace;
}
