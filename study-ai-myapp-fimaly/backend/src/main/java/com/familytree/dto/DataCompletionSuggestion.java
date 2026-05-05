package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataCompletionSuggestion {
    private String type; // field, relationship, event, etc.
    private Long memberId;
    private String memberName;
    private String suggestion;
    private String urgency; // low, medium, high
}
