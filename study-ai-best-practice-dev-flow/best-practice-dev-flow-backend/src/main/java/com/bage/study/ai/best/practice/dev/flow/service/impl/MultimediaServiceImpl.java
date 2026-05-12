package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.response.MultimediaDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.Multimedia;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.MultimediaRepository;
import com.bage.study.ai.best.practice.dev.flow.service.MultimediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultimediaServiceImpl implements MultimediaService {

    private final MultimediaRepository multimediaRepository;

    @Override
    public MultimediaDTO upload(Long familyId, Long memberId, String type, String url, String description, Long uploaderId) {
        log.info("上传媒体文件: familyId={}, type={}", familyId, type);

        Multimedia media = new Multimedia();
        media.setFamilyId(familyId);
        media.setMemberId(memberId);
        media.setType(type);
        media.setUrl(url);
        media.setDescription(description);
        media.setUploaderId(uploaderId);

        Multimedia savedMedia = multimediaRepository.save(media);
        log.info("媒体文件上传成功: mediaId={}", savedMedia.getId());

        return convertToDTO(savedMedia);
    }

    @Override
    public void delete(Long mediaId) {
        log.info("删除媒体文件: mediaId={}", mediaId);

        Multimedia media = multimediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("媒体文件不存在"));

        media.setDeleted(1);
        multimediaRepository.save(media);
        log.info("媒体文件删除成功: mediaId={}", mediaId);
    }

    @Override
    public MultimediaDTO getById(Long mediaId) {
        Multimedia media = multimediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("媒体文件不存在"));
        return convertToDTO(media);
    }

    @Override
    public List<MultimediaDTO> getByFamily(Long familyId) {
        return multimediaRepository.findByFamilyId(familyId).stream()
                .filter(m -> m.getDeleted() == 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MultimediaDTO> getByMember(Long memberId) {
        return multimediaRepository.findByMemberId(memberId).stream()
                .filter(m -> m.getDeleted() == 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MultimediaDTO convertToDTO(Multimedia media) {
        MultimediaDTO dto = new MultimediaDTO();
        dto.setId(media.getId());
        dto.setFamilyId(media.getFamilyId());
        dto.setMemberId(media.getMemberId());
        dto.setType(media.getType());
        dto.setUrl(media.getUrl());
        dto.setDescription(media.getDescription());
        dto.setUploadTime(media.getUploadTime());
        dto.setUploaderId(media.getUploaderId());
        return dto;
    }
}