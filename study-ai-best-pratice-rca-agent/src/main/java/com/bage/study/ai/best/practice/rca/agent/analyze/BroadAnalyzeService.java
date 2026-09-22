package com.bage.study.ai.best.practice.rca.agent.analyze;

import com.bage.study.ai.best.practice.rca.agent.llm.LlmGateway;
import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.Verdict;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BroadAnalyze：对 BroadScan 证据做裁决 + 首轮置信度更新。
 * LLM（若启用且请求允许）只做"证据解读叙述"，不触碰打分与裁决。
 */
@Service
public class BroadAnalyzeService {

    private static final Logger log = LoggerFactory.getLogger(BroadAnalyzeService.class);

    private final EvidenceEvaluator evaluator;
    private final HypothesisScorer scorer;
    private final LlmGateway gateway;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BroadAnalyzeService(EvidenceEvaluator evaluator, HypothesisScorer scorer, LlmGateway gateway) {
        this.evaluator = evaluator;
        this.scorer = scorer;
        this.gateway = gateway;
    }

    /** @return 本次分析是否实际使用了 LLM */
    public boolean analyze(List<Hypothesis> hypotheses, List<Evidence> allEvidences, boolean llmAllowed) {
        Map<String, List<Evidence>> byHypothesis = groupById(allEvidences);
        for (Hypothesis h : hypotheses) {
            List<Evidence> broadOnly = byHypothesis.getOrDefault(h.getId(), List.of()).stream()
                    .filter(e -> e.getPhase() == Evidence.Phase.BROAD)
                    .toList();
            evaluator.evaluate(h, broadOnly);
            h.setConfidence(scorer.score(h, broadOnly));
            h.setRationale(templateRationale(h, broadOnly));
        }

        if (llmAllowed && gateway.isEnabled()) {
            return narrateByLlm(hypotheses, byHypothesis);
        }
        return false;
    }

    boolean narrateByLlm(List<Hypothesis> hypotheses, Map<String, List<Evidence>> byHypothesis) {
        try {
            StringBuilder table = new StringBuilder();
            for (Hypothesis h : hypotheses) {
                String tools = byHypothesis.getOrDefault(h.getId(), List.of()).stream()
                        .map(e -> e.getToolName() + "(异常=" + e.isAnomaly() + ",偏离" + e.getDeviationRatio() + "x)")
                        .collect(Collectors.joining(", "));
                table.append(String.format("- %s [%s] %s；证据: %s%n",
                        h.getId(), h.playbookKey(), h.getDesc(), tools));
            }
            String prompt = """
                    以下是根因假设及其采集证据。请为每个假设写一句证据解读（只能复述证据事实，禁止引入表外信息，必须提及具体工具名）。
                    只输出 JSON 对象，key 为假设 id，value 为一句话解读，不要输出 Markdown。

                    %s""".formatted(table);
            String raw = gateway.complete(prompt);
            if (raw == null || raw.isBlank()) {
                return false;
            }
            String json = raw.trim();
            if (json.startsWith("```")) {
                int firstNl = json.indexOf('\n');
                json = firstNl > 0 ? json.substring(firstNl + 1) : json;
                json = json.endsWith("```") ? json.substring(0, json.length() - 3) : json;
            }
            JsonNode node = objectMapper.readTree(json.trim());
            boolean any = false;
            for (Hypothesis h : hypotheses) {
                String text = node.path(h.getId()).asText("");
                if (!text.isBlank()) {
                    h.setRationale(text.trim());
                    any = true;
                }
            }
            return any;
        } catch (Exception e) {
            log.warn("LLM evidence narration failed, keep deterministic rationale: {}", e.getMessage());
            return false;
        }
    }

    static String templateRationale(Hypothesis h, List<Evidence> evidences) {
        List<String> supportTools = evidences.stream()
                .filter(e -> e.getVerdict() == Verdict.SUPPORT)
                .map(Evidence::getToolName).toList();
        List<String> refuteTools = evidences.stream()
                .filter(e -> e.getVerdict() == Verdict.REFUTE)
                .map(Evidence::getToolName).toList();
        double maxDeviation = evidences.stream()
                .filter(e -> e.getVerdict() == Verdict.SUPPORT)
                .mapToDouble(Evidence::getDeviationRatio).max().orElse(1.0);

        return String.format("支持证据 %d 条 %s（最大偏离 %.1fx），反对证据 %d 条 %s；首轮置信度 %.2f",
                supportTools.size(), supportTools, maxDeviation,
                refuteTools.size(), refuteTools, h.getConfidence());
    }

    private Map<String, List<Evidence>> groupById(List<Evidence> evidences) {
        Map<String, List<Evidence>> map = new LinkedHashMap<>();
        for (Evidence e : evidences) {
            map.computeIfAbsent(e.getHypothesisId(), k -> new java.util.ArrayList<>()).add(e);
        }
        return map;
    }
}
