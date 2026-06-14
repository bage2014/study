package com.bage.study.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "platform")
public class PlatformApiConfig {

    private TaobaoConfig taobao = new TaobaoConfig();
    private JdConfig jd = new JdConfig();
    private MeituanConfig meituan = new MeituanConfig();

    @Data
    public static class TaobaoConfig {
        private boolean enabled = false;
        private String appKey;
        private String appSecret;
        private String baseUrl = "https://eco.taobao.com/router/rest";
        private String apiVersion = "2.0";
    }

    @Data
    public static class JdConfig {
        private boolean enabled = false;
        private String appKey;
        private String appSecret;
        private String accessToken;
        private String baseUrl = "https://api.jd.com/routerjson";
    }

    @Data
    public static class MeituanConfig {
        private boolean enabled = false;
        private String appKey;
        private String appSecret;
        private String baseUrl = "https://api.meituan.com";
    }
}