package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.core.activity.WriteAndCommitActivity;
import com.bage.ai.pipeline.core.dto.activity.WriteAndCommitInput;
import com.bage.ai.pipeline.core.dto.activity.WriteAndCommitResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@Component
public class WriteAndCommitActivityImpl implements WriteAndCommitActivity {

    @Override
    public WriteAndCommitResult writeAndCommit(WriteAndCommitInput input) {
        log.info("Starting write and commit for pipeline: {}", input.getRunId());

        for (Map.Entry<String, String> entry : input.getGeneratedFiles().entrySet()) {
            String filePath = entry.getKey();
            String content = entry.getValue();

            try {
                Path fullPath = Paths.get(input.getProjectLocalPath(), filePath);
                Files.createDirectories(fullPath.getParent());
                Files.writeString(fullPath, content);
                log.info("Wrote file: {}", fullPath);
            } catch (IOException e) {
                log.error("Failed to write file: {}", filePath, e);
                return WriteAndCommitResult.builder()
                        .runId(input.getRunId())
                        .build();
            }
        }

        log.info("Write and commit completed for pipeline: {}", input.getRunId());
        return WriteAndCommitResult.builder()
                .runId(input.getRunId())
                .build();
    }
}