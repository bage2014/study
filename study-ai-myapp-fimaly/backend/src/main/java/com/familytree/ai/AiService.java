package com.familytree.ai;

import com.familytree.config.AiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final AiProperties aiProperties;
    private final List<AiProvider> providers;

    private AiProvider getActiveProvider() {
        if (!aiProperties.isEnabled()) {
            log.warn("[AI服务] AI功能未启用");
            return null;
        }

        List<AiProvider> availableProviders = providers.stream()
                .filter(AiProvider::isAvailable)
                .collect(Collectors.toList());

        if (availableProviders.isEmpty()) {
            log.warn("[AI服务] 没有可用的AI Provider");
            return null;
        }

        String preferredProvider = aiProperties.getProviders().isEmpty() ? null :
                aiProperties.getProviders().keySet().stream().findFirst().orElse(null);

        if (preferredProvider != null) {
            AiProvider provider = availableProviders.stream()
                    .filter(p -> p.getProviderName().equals(preferredProvider))
                    .findFirst()
                    .orElse(availableProviders.get(0));
            log.info("[AI服务] 使用Provider: {}", provider.getProviderName());
            return provider;
        }

        return availableProviders.get(0);
    }

    public Map<String, Object> predictRelationship(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            AiProvider provider = getActiveProvider();
            if (provider == null) {
                result.put("success", false);
                result.put("error", "AI服务不可用");
                return result;
            }

            AiProvider.AiRequest request = new AiProvider.AiRequest() {
                @Override
                public String getType() {
                    return "relationship";
                }

                @Override
                public Map<String, Object> getParams() {
                    return params;
                }
            };

            AiProvider.AiResponse response = provider.predict(request);

            result.put("success", response.isSuccess());
            if (response.isSuccess()) {
                result.put("content", response.getContent());
                result.put("metadata", response.getMetadata());
            } else {
                result.put("error", response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[AI服务] 关系预测失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    public Map<String, Object> analyzeName(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        Map<String, Object> result = new HashMap<>();

        try {
            AiProvider provider = getActiveProvider();
            if (provider == null) {
                result.put("success", false);
                result.put("error", "AI服务不可用");
                return result;
            }

            AiProvider.AiRequest request = new AiProvider.AiRequest() {
                @Override
                public String getType() {
                    return "name_analysis";
                }

                @Override
                public Map<String, Object> getParams() {
                    return params;
                }
            };

            AiProvider.AiResponse response = provider.predict(request);

            result.put("success", response.isSuccess());
            if (response.isSuccess()) {
                result.put("content", response.getContent());
                result.put("metadata", response.getMetadata());
            } else {
                result.put("error", response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[AI服务] 姓名分析失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    public Map<String, Object> generateFamilyStory(String familyName) {
        Map<String, Object> params = new HashMap<>();
        params.put("familyName", familyName);

        Map<String, Object> result = new HashMap<>();

        try {
            AiProvider provider = getActiveProvider();
            if (provider == null) {
                result.put("success", false);
                result.put("error", "AI服务不可用");
                return result;
            }

            AiProvider.AiStoryRequest request = new AiProvider.AiStoryRequest() {
                @Override
                public String getType() {
                    return "family_story";
                }

                @Override
                public Map<String, Object> getParams() {
                    return params;
                }
            };

            AiProvider.AiResponse response = provider.generateStory(request);

            result.put("success", response.isSuccess());
            if (response.isSuccess()) {
                result.put("content", response.getContent());
                result.put("metadata", response.getMetadata());
            } else {
                result.put("error", response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[AI服务] 生成家族故事失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    public Map<String, Object> analyzeImage(String imageBase64, String imageName) {
        Map<String, Object> result = new HashMap<>();

        try {
            AiProvider provider = getActiveProvider();
            if (provider == null) {
                result.put("success", false);
                result.put("error", "AI服务不可用");
                return result;
            }

            AiProvider.AiResponse response = provider.analyzeImage(imageBase64, imageName);

            result.put("success", response.isSuccess());
            if (response.isSuccess()) {
                result.put("content", response.getContent());
                result.put("metadata", response.getMetadata());
            } else {
                result.put("error", response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[AI服务] 图片分析失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    public Map<String, Object> getServiceStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("enabled", aiProperties.isEnabled());
        status.put("timeout", aiProperties.getTimeoutMs());
        status.put("maxRetries", aiProperties.getMaxRetries());

        List<Map<String, Object>> providerList = providers.stream()
                .map(p -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("name", p.getProviderName());
                    info.put("available", p.isAvailable());
                    return info;
                })
                .collect(Collectors.toList());

        status.put("providers", providerList);
        return status;
    }
}