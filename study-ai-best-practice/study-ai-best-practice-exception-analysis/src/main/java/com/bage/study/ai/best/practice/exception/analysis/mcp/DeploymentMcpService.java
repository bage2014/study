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
        
        DeploymentRecord record1 = new DeploymentRecord();
        record1.setDeploymentId("DEPLOY-" + System.currentTimeMillis());
        record1.setAppId(appId);
        record1.setVersion("1.2.3");
        record1.setStatus("SUCCESS");
        record1.setDeployTime(LocalDateTime.now().minusHours(2));
        record1.setChanges("修复用户登录问题，优化数据库查询性能");
        record1.setOperator("admin");
        records.add(record1);

        if (random.nextBoolean()) {
            DeploymentRecord record2 = new DeploymentRecord();
            record2.setDeploymentId("DEPLOY-" + (System.currentTimeMillis() - 1000));
            record2.setAppId(appId);
            record2.setVersion("1.2.2");
            record2.setStatus("SUCCESS");
            record2.setDeployTime(LocalDateTime.now().minusDays(1));
            record2.setChanges("新增支付功能模块");
            record2.setOperator("developer");
            records.add(record2);
        }

        return records;
    }
}
