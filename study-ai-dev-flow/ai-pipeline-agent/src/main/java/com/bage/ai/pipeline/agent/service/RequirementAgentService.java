package com.bage.ai.pipeline.agent.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RequirementAgentService {

    private final RequirementAiService aiService;

    public RequirementAgentService(@Qualifier("requirementModel") ChatLanguageModel model) {
        this.aiService = AiServices.builder(RequirementAiService.class)
                .chatLanguageModel(model)
                .build();
    }

    public String analyze(String prdMarkdown) {
        return aiService.analyze(prdMarkdown);
    }

    interface RequirementAiService {

        @SystemMessage("""
                You are a senior software requirement analyst.
                Analyze the given PRD (Product Requirements Document) and extract structured requirements.

                Output a valid JSON object with this structure:
                {
                  "summary": "one-sentence overview",
                  "features": [
                    {
                      "name": "feature name",
                      "module": "target module or package",
                      "description": "what needs to be implemented",
                      "acceptance_criteria": ["criterion 1", "criterion 2"]
                    }
                  ],
                  "affected_files_hints": ["optional file path hints if obvious"]
                }

                Output ONLY the JSON object. No explanation, no markdown fences.
                """)
        @UserMessage("PRD Content:\n{{prd}}")
        String analyze(@V("prd") String prd);
    }
}