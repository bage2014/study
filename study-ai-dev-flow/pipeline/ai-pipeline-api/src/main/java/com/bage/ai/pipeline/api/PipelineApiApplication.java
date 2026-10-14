package com.bage.ai.pipeline.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bage.ai.pipeline"})
public class PipelineApiApplication {

    public static void main(String[] args) {
        loadEnvVariables();
        SpringApplication.run(PipelineApiApplication.class, args);
    }

    private static void loadEnvVariables() {
        try {
            String userDir = System.getProperty("user.dir");
            java.io.File envFile = new java.io.File(userDir, ".env");
            if (!envFile.exists()) {
                envFile = new java.io.File(userDir + "/..", ".env");
            }
            if (!envFile.exists()) {
                envFile = new java.io.File(userDir + "/../..", ".env");
            }
            
            Dotenv dotenv;
            if (envFile.exists()) {
                dotenv = Dotenv.configure().directory(envFile.getParent()).ignoreIfMissing().load();
                System.out.println("Loaded .env file from: " + envFile.getAbsolutePath());
            } else {
                dotenv = Dotenv.configure().ignoreIfMissing().load();
                System.out.println(".env file not found, using system environment variables");
            }
            
            System.setProperty("AI_DEEPSEEK_API_KEY", dotenv.get("AI_DEEPSEEK_API_KEY", ""));
            System.setProperty("AI_DEEPSEEK_BASE_URL", dotenv.get("AI_DEEPSEEK_BASE_URL", "https://api.deepseek.com/v1"));
            System.setProperty("AI_REQUIREMENT_MODEL_PROVIDER", dotenv.get("AI_REQUIREMENT_MODEL_PROVIDER", "deepseek"));
            System.setProperty("AI_CODEGEN_MODEL_PROVIDER", dotenv.get("AI_CODEGEN_MODEL_PROVIDER", "deepseek"));
            System.setProperty("AI_REQUIREMENT_MODEL", dotenv.get("AI_REQUIREMENT_MODEL", "deepseek-chat"));
            System.setProperty("AI_CODEGEN_MODEL", dotenv.get("AI_CODEGEN_MODEL", "deepseek-chat"));
        } catch (Exception e) {
            System.err.println("Failed to load .env file, using system environment variables: " + e.getMessage());
        }
    }
}