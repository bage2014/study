package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.response.MultimediaDTO;

import java.util.List;

public interface MultimediaService {

    MultimediaDTO upload(Long familyId, Long memberId, String type, String url, String description, Long uploaderId);

    void delete(Long mediaId);

    MultimediaDTO getById(Long mediaId);

    List<MultimediaDTO> getByFamily(Long familyId);

    List<MultimediaDTO> getByMember(Long memberId);
}