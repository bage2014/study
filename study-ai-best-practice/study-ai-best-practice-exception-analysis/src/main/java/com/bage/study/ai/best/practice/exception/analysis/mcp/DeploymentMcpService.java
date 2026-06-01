package com.bage.study.ai.best.practice.exception.analysis.mcp;

import com.bage.study.ai.best.practice.exception.analysis.context.DeploymentRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DeploymentMcpService {

    private final Random random = new Random();

    public List<DeploymentRecord> getDeploymentRecords(String appId, LocalDateTime startTime, LocalDateTime endTime) {
        List<DeploymentRecord> records = new ArrayList<>();
        
        int count = random.nextInt(3) + 1;
        for (int i = 0; i < count; i++) {
            LocalDateTime deployTime = startTime != null ? 
                startTime.plusMinutes(random.nextInt(120)) : 
                LocalDateTime.now().minusHours(random.nextInt(24));
            
            DeploymentRecord record = new DeploymentRecord(
                "DEPLOY-" + System.currentTimeMillis() + "-" + i,
                appId,
                "1." + (random.nextInt(10) + 1) + "." + (random.nextInt(100)),
                i == 0 ? "SUCCESS" : random.nextBoolean() ? "SUCCESS" : "FAILED",
                deployTime,
                "user" + random.nextInt(100),
                "部署版本更新"
            );
            records.add(record);
        }
        
        return records;
    }

    public DeploymentRecord getLatestDeployment(String appId) {
        return new DeploymentRecord(
            "DEPLOY-" + System.currentTimeMillis(),
            appId,
            "1.2.3",
            "SUCCESS",
            LocalDateTime.now().minusHours(random.nextInt(3)),
            "admin",
            "紧急修复发布"
        );
    }
}