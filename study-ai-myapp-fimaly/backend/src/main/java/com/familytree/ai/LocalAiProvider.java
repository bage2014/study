package com.familytree.ai;

import com.familytree.config.AiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalAiProvider implements AiProvider {

    private final AiProperties aiProperties;

    @Override
    public String getProviderName() {
        return "local";
    }

    @Override
    public boolean isAvailable() {
        return aiProperties.isEnabled();
    }

    @Override
    public AiResponse predict(AiRequest request) {
        log.info("[LocalAI] 处理预测请求 type={}", request.getType());

        try {
            Map<String, Object> params = request.getParams();
            String type = request.getType();

            String result;
            if ("relationship".equals(type)) {
                result = predictRelationship(params);
            } else if ("name_analysis".equals(type)) {
                result = analyzeName(params);
            } else {
                result = "未知预测类型";
            }

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("provider", getProviderName());
            metadata.put("model", "local-rule-based");
            metadata.put("processingTimeMs", System.currentTimeMillis());

            return new AiResponse() {
                @Override
                public boolean isSuccess() {
                    return true;
                }

                @Override
                public String getContent() {
                    return result;
                }

                @Override
                public String getErrorMessage() {
                    return null;
                }

                @Override
                public Map<String, Object> getMetadata() {
                    return metadata;
                }
            };
        } catch (Exception e) {
            log.error("[LocalAI] 预测请求失败 type={}, error={}", request.getType(), e.getMessage());
            return new AiResponse() {
                @Override
                public boolean isSuccess() {
                    return false;
                }

                @Override
                public String getContent() {
                    return null;
                }

                @Override
                public String getErrorMessage() {
                    return e.getMessage();
                }

                @Override
                public Map<String, Object> getMetadata() {
                    return null;
                }
            };
        }
    }

    @Override
    public AiResponse generateStory(AiStoryRequest request) {
        log.info("[LocalAI] 生成故事请求 type={}", request.getType());

        try {
            Map<String, Object> params = request.getParams();
            String type = request.getType();

            String story = generateFamilyStory(type, params);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("provider", getProviderName());
            metadata.put("storyType", type);

            return new AiResponse() {
                @Override
                public boolean isSuccess() {
                    return true;
                }

                @Override
                public String getContent() {
                    return story;
                }

                @Override
                public String getErrorMessage() {
                    return null;
                }

                @Override
                public Map<String, Object> getMetadata() {
                    return metadata;
                }
            };
        } catch (Exception e) {
            log.error("[LocalAI] 生成故事失败 type={}, error={}", request.getType(), e.getMessage());
            return new AiResponse() {
                @Override
                public boolean isSuccess() {
                    return false;
                }

                @Override
                public String getContent() {
                    return null;
                }

                @Override
                public String getErrorMessage() {
                    return e.getMessage();
                }

                @Override
                public Map<String, Object> getMetadata() {
                    return null;
                }
            };
        }
    }

    private String predictRelationship(Map<String, Object> params) {
        String name1 = (String) params.get("name1");
        String name2 = (String) params.get("name2");
        Integer ageDiff = (Integer) params.get("ageDiff");

        if (ageDiff == null) {
            return "关系预测需要年龄差信息";
        }

        if (ageDiff >= 20 && ageDiff <= 40) {
            return String.format("%s 可能是 %s 的父母", name1, name2);
        } else if (ageDiff <= 15) {
            return String.format("%s 和 %s 可能是兄弟姐妹", name1, name2);
        } else {
            return String.format("%s 和 %s 可能是亲戚关系", name1, name2);
        }
    }

    private String analyzeName(Map<String, Object> params) {
        String name = (String) params.get("name");
        if (name == null || name.isEmpty()) {
            return "请提供姓名进行分析";
        }
        return String.format("姓名 '%s' 分析完成", name);
    }

    private String generateFamilyStory(String type, Map<String, Object> params) {
        String familyName = (String) params.get("familyName");
        if (familyName == null) {
            familyName = "家族";
        }

        return String.format("从前有一个叫做%s的家族，他们世世代代生活在一起，传承着美好的家风家训。", familyName);
    }

    @Override
    public AiResponse analyzeImage(String imageBase64, String imageName) {
        log.info("[LocalAI] 处理图片分析请求 imageName={}", imageName);

        try {
            // 模拟图片分析结果，返回一个预设的家族关系图解析结果
            String mockResult = """
                    成员列表：
                    - 张三, 男
                    - 李四, 女
                    - 张小明, 男
                    - 张小红, 女
                    
                    关系列表：
                    - 张三 与 李四 的关系: 配偶
                    - 张三 与 张小明 的关系: 父子
                    - 张三 与 张小红 的关系: 父女
                    - 李四 与 张小明 的关系: 母子
                    - 李四 与 张小红 的关系: 母女
                    - 张小明 与 张小红 的关系: 兄弟姐妹
                    """;

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("provider", getProviderName());
            metadata.put("imageName", imageName);

            return new AiResponse() {
                @Override
                public boolean isSuccess() {
                    return true;
                }

                @Override
                public String getContent() {
                    return mockResult;
                }

                @Override
                public String getErrorMessage() {
                    return null;
                }

                @Override
                public Map<String, Object> getMetadata() {
                    return metadata;
                }
            };
        } catch (Exception e) {
            log.error("[LocalAI] 图片分析失败 error={}", e.getMessage());
            return new AiResponse() {
                @Override
                public boolean isSuccess() {
                    return false;
                }

                @Override
                public String getContent() {
                    return null;
                }

                @Override
                public String getErrorMessage() {
                    return e.getMessage();
                }

                @Override
                public Map<String, Object> getMetadata() {
                    return null;
                }
            };
        }
    }
}