package com.bage.study.ai.best.practice.exceptionanalysis.service;

import com.bage.study.ai.best.practice.exceptionanalysis.model.ProblemAnalysisRequest;
import com.bage.study.ai.best.practice.exceptionanalysis.model.ProblemAnalysisResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnalysisService {

    private final PlanService planService;
    private final ExecuteService executeService;
    private final ReactService reactService;

    private final Map<String, ProblemAnalysisResponse> analysisCache = new ConcurrentHashMap<>();

    public AnalysisService(PlanService planService, 
                         ExecuteService executeService, 
                         ReactService reactService) {
        this.planService = planService;
        this.executeService = executeService;
        this.reactService = reactService;
    }

    public ProblemAnalysisResponse analyze(ProblemAnalysisRequest request) {
        var plan = planService.generatePlan(request);
        var executedPlan = executeService.executePlan(plan);
        var response = reactService.analyzeResults(request, executedPlan);

        analysisCache.put(response.analysisId(), response);
        return response;
    }

    public ProblemAnalysisResponse reanalyze(String analysisId, String feedback) {
        var previousResponse = analysisCache.get(analysisId);
        if (previousResponse == null) {
            return ProblemAnalysisResponse.error(analysisId, "Analysis not found");
        }

        var request = ProblemAnalysisRequest.of(
            "Reanalysis with feedback",
            "Previous analysis: " + previousResponse.conclusion()
        );

        var newResponse = reactService.reanalyzeWithFeedback(
            request, previousResponse, feedback
        );

        analysisCache.put(newResponse.analysisId(), newResponse);
        return newResponse;
    }

    public ProblemAnalysisResponse getAnalysis(String analysisId) {
        return analysisCache.get(analysisId);
    }

    public Map<String, ProblemAnalysisResponse> getRecentAnalyses() {
        return Map.copyOf(analysisCache);
    }

    public void clearCache() {
        analysisCache.clear();
    }
}
