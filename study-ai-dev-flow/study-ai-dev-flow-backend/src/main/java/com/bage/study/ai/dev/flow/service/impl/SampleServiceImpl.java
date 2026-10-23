package com.bage.study.ai.dev.flow.service.impl;

import com.bage.study.ai.dev.flow.dto.request.SampleCreateRequest;
import com.bage.study.ai.dev.flow.dto.request.SampleUpdateRequest;
import com.bage.study.ai.dev.flow.dto.response.SampleResponse;
import com.bage.study.ai.dev.flow.entity.SampleEntity;
import com.bage.study.ai.dev.flow.exception.BusinessException;
import com.bage.study.ai.dev.flow.repository.SampleRepository;
import com.bage.study.ai.dev.flow.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    @Override
    public List<SampleResponse> findAll() {
        log.info("查询所有示例数据");
        return sampleRepository.findAll().stream()
                .map(SampleResponse::fromEntity)
                .toList();
    }

    @Override
    public SampleResponse findById(Long id) {
        log.info("根据ID查询示例, id: {}", id);
        SampleEntity entity = sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("NOT_FOUND", "示例数据不存在, id: " + id));
        return SampleResponse.fromEntity(entity);
    }

    @Override
    @Transactional
    public SampleResponse create(SampleCreateRequest request) {
        log.info("创建示例, name: {}", request.getName());
        SampleEntity entity = new SampleEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        SampleEntity saved = sampleRepository.save(entity);
        log.debug("示例创建成功, id: {}", saved.getId());
        return SampleResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public SampleResponse update(Long id, SampleUpdateRequest request) {
        log.info("更新示例, id: {}", id);
        SampleEntity entity = sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("NOT_FOUND", "示例数据不存在, id: " + id));
        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        SampleEntity updated = sampleRepository.save(entity);
        return SampleResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("删除示例, id: {}", id);
        if (!sampleRepository.existsById(id)) {
            throw new BusinessException("NOT_FOUND", "示例数据不存在, id: " + id);
        }
        sampleRepository.deleteById(id);
    }
}
