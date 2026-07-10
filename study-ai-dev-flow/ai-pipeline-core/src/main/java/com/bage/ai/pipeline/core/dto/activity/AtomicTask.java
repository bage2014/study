package com.bage.ai.pipeline.core.dto.activity;

import com.bage.ai.pipeline.core.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtomicTask {
    private String id;
    private String featurePointId;
    private String title;
    private String description;
    private List<String> targetFiles;
    private TaskType type;
}