package com.bage.study.ai.best.practice.exception.analysis.service;

import com.bage.study.ai.best.practice.exception.analysis.dto.request.AnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse;

public interface AnalysisService {
    AnalysisResponse analyze(AnalysisRequest request);
}
