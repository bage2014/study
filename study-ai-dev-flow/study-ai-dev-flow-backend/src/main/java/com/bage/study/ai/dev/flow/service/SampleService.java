package com.bage.study.ai.dev.flow.service;

import com.bage.study.ai.dev.flow.dto.request.SampleCreateRequest;
import com.bage.study.ai.dev.flow.dto.request.SampleUpdateRequest;
import com.bage.study.ai.dev.flow.dto.response.SampleResponse;

import java.util.List;

public interface SampleService {

    List<SampleResponse> findAll();

    SampleResponse findById(Long id);

    SampleResponse create(SampleCreateRequest request);

    SampleResponse update(Long id, SampleUpdateRequest request);

    void delete(Long id);
}
