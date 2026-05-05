package com.familytree.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.familytree.config.AiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class QwenAiProvider implements AiProvider {

    private final AiProperties aiProperties;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getProviderName() {
        return "qwen";
    }

    @Override
    public boolean isAvailable() {
        AiProperties.ProviderConfig config = aiProperties.getProviders().get("qwen");
        return config != null && config.getApiKey() != null && !config.getApiKey().isEmpty();
    }

    @Override
    public AiResponse predict(AiRequest request) {
        try {
            String prompt = buildPredictPrompt(request.getParams());
            String content = callQwenApi(prompt);
            return SimpleAiResponse.success(content);
        } catch (Exception e) {
            log.error("[通义千问] 关系预测失败", e);
            return SimpleAiResponse.error("预测失败: " + e.getMessage());
        }
    }

    @Override
    public AiResponse generateStory(AiStoryRequest request) {
        try {
            String prompt = buildStoryPrompt(request.getParams());
            String content = callQwenApi(prompt);
            return SimpleAiResponse.success(content);
        } catch (Exception e) {
            log.error("[通义千问] 故事生成失败", e);
            return SimpleAiResponse.error("生成失败: " + e.getMessage());
        }
    }

    private String buildPredictPrompt(Map<String, Object> params) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个专业的族谱关系分析专家。\n\n");

        if (params.containsKey("member1") && params.containsKey("member2")) {
            prompt.append("请分析以下两位家族成员之间可能的关系：\n\n");

            Object member1 = params.get("member1");
            Object member2 = params.get("member2");

            if (member1 instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> m1 = (Map<String, Object>) member1;
                prompt.append("成员1：\n");
                appendMemberInfo(prompt, m1);
            }

            if (member2 instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> m2 = (Map<String, Object>) member2;
                prompt.append("\n成员2：\n");
                appendMemberInfo(prompt, m2);
            }

            prompt.append("\n请分析并输出：\n");
            prompt.append("1. 最可能的关系（父子/母子/兄弟姐妹/祖孙/配偶等）\n");
            prompt.append("2. 置信度（0-100%）\n");
            prompt.append("3. 分析依据\n");
        } else {
            prompt.append("请简要说明如何分析族谱成员关系。");
        }

        return prompt.toString();
    }

    private void appendMemberInfo(StringBuilder prompt, Map<String, Object> member) {
        if (member.containsKey("name")) {
            prompt.append("- 姓名：").append(member.get("name")).append("\n");
        }
        if (member.containsKey("birthYear")) {
            prompt.append("- 出生年份：").append(member.get("birthYear")).append("\n");
        }
        if (member.containsKey("gender")) {
            prompt.append("- 性别：").append(member.get("gender")).append("\n");
        }
    }

    private String buildStoryPrompt(Map<String, Object> params) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个专业的家族历史故事创作专家。\n\n");

        Object familyName = params.getOrDefault("familyName", "这个家族");
        Object storyType = params.getOrDefault("storyType", "migration");

        prompt.append("请为").append(familyName).append("家族创作一篇");

        if ("migration".equals(storyType)) {
            prompt.append("迁徙历史故事");
        } else if ("biography".equals(storyType)) {
            prompt.append("人物传记");
        } else if ("legend".equals(storyType)) {
            prompt.append("传奇故事");
        } else {
            prompt.append("家族故事");
        }

        prompt.append("。\n\n要求：\n");
        prompt.append("- 内容生动有趣，富有情感\n");
        prompt.append("- 语言符合中国文化背景\n");
        prompt.append("- 长度500-1000字\n");
        prompt.append("- 使用markdown格式，有标题和段落\n");

        if (params.containsKey("members")) {
            prompt.append("\n已知家族成员信息：\n");
            prompt.append(params.get("members"));
        }

        return prompt.toString();
    }

    private String callQwenApi(String prompt) {
        AiProperties.ProviderConfig config = aiProperties.getProviders().get("qwen");
        if (config == null || config.getApiKey() == null || config.getApiKey().isEmpty()) {
            throw new IllegalStateException("通义千问API配置未设置");
        }

        String apiKey = config.getApiKey();
        String model = config.getModel() != null ? config.getModel() : "qwen-turbo";
        String endpoint = config.getEndpoint() != null ? config.getEndpoint() : "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        WebClient webClient = webClientBuilder.build();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);

        Map<String, Object> input = new HashMap<>();
        input.put("messages", new Object[]{
                Map.of("role", "system", "content", "你是一个专业的家族历史分析专家，擅长分析族谱关系和创作家族故事。"),
                Map.of("role", "user", "content", prompt)
        });
        requestBody.put("input", input);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 0.7);
        parameters.put("max_tokens", 1500);
        requestBody.put("parameters", parameters);

        try {
            Mono<String> responseMono = webClient.post()
                    .uri(endpoint)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class);

            String response = responseMono.block();

            if (response != null) {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode outputNode = rootNode.path("output");
                JsonNode choicesNode = outputNode.path("choices");
                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode firstChoice = choicesNode.get(0);
                    JsonNode messageNode = firstChoice.path("message");
                    JsonNode contentNode = messageNode.path("content");
                    return contentNode.asText();
                }
            }

            return "API响应异常";
        } catch (Exception e) {
            log.error("[通义千问] API调用失败", e);
            throw new RuntimeException("API调用失败", e);
        }
    }
}
