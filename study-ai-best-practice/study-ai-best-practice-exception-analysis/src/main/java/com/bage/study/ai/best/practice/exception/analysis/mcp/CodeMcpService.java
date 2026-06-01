package com.bage.study.ai.best.practice.exception.analysis.mcp;

import com.bage.study.ai.best.practice.exception.analysis.context.CodeInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class CodeMcpService {

    private final Random random = new Random();

    private static final String[] COMMIT_MESSAGES = {
        "修复登录认证问题",
        "优化用户查询性能",
        "添加新功能模块",
        "修复数据库连接问题",
        "更新依赖版本",
        "重构登录模块",
        "修复并发问题",
        "添加监控指标"
    };

    private static final String[] CHANGED_FILES = {
        "src/main/java/com/example/service/UserService.java, src/main/java/com/example/controller/UserController.java",
        "src/main/java/com/example/repository/UserRepository.java",
        "src/main/java/com/example/config/SecurityConfig.java, src/main/java/com/example/filter/AuthFilter.java",
        "src/main/resources/application.yml",
        "src/main/java/com/example/service/LoginService.java",
        "pom.xml",
        "src/main/java/com/example/util/TokenUtil.java"
    };

    private static final String[] CHANGE_SUMMARIES = {
        "修改了用户认证逻辑，更新了JWT token生成方式",
        "优化了数据库查询语句，添加了索引",
        "新增了用户注册功能模块",
        "修复了连接池配置问题，调整了最大连接数",
        "更新了Spring Boot版本至3.2.0",
        "重构了登录流程，分离了认证逻辑",
        "修复了多线程环境下的数据竞争问题",
        "添加了Prometheus监控指标"
    };

    public CodeInfo getCodeInfo(String appId) {
        int idx = random.nextInt(COMMIT_MESSAGES.length);
        
        return new CodeInfo(
            appId,
            "https://github.com/example/" + appId + ".git",
            "main",
            generateCommitId(),
            LocalDateTime.now().minusHours(random.nextInt(24)),
            COMMIT_MESSAGES[idx],
            "developer" + random.nextInt(10),
            CHANGED_FILES[idx],
            CHANGE_SUMMARIES[idx]
        );
    }

    private String generateCommitId() {
        StringBuilder sb = new StringBuilder();
        String chars = "abcdef0123456789";
        for (int i = 0; i < 7; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}