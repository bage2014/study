package com.bage.study.ai.dev.flow.controller;

import com.bage.study.ai.dev.flow.dto.request.SampleCreateRequest;
import com.bage.study.ai.dev.flow.dto.request.SampleUpdateRequest;
import com.bage.study.ai.dev.flow.dto.response.SampleResponse;
import com.bage.study.ai.dev.flow.service.SampleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/samples")
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping
    public ResponseEntity<List<SampleResponse>> findAll() {
        log.debug("查询所有示例");
        return ResponseEntity.ok(sampleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleResponse> findById(@PathVariable Long id) {
        log.debug("查询示例, id: {}", id);
        return ResponseEntity.ok(sampleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SampleResponse> create(@Valid @RequestBody SampleCreateRequest request) {
        log.info("创建示例, name: {}", request.getName());
        SampleResponse response = sampleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<SampleResponse> update(@PathVariable Long id,
                                                  @RequestBody SampleUpdateRequest request) {
        log.info("更新示例, id: {}", id);
        return ResponseEntity.ok(sampleService.update(id, request));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("删除示例, id: {}", id);
        sampleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
