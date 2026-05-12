package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.response.MultimediaDTO;
import com.bage.study.ai.best.practice.dev.flow.service.MultimediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Slf4j
public class MultimediaController {

    private final MultimediaService multimediaService;

    @PostMapping
    public ResponseEntity<RestResult<MultimediaDTO>> upload(
            @RequestParam Long familyId,
            @RequestParam(required = false) Long memberId,
            @RequestParam String type,
            @RequestParam String url,
            @RequestParam(required = false) String description,
            @RequestAttribute("userId") Long uploaderId) {
        MultimediaDTO media = multimediaService.upload(familyId, memberId, type, url, description, uploaderId);
        return ResponseEntity.ok(RestResult.success("上传成功", media));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Void>> delete(@PathVariable Long id) {
        multimediaService.delete(id);
        return ResponseEntity.ok(RestResult.success(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResult<MultimediaDTO>> getById(@PathVariable Long id) {
        MultimediaDTO media = multimediaService.getById(id);
        return ResponseEntity.ok(RestResult.success(media));
    }

    @GetMapping("/family")
    public ResponseEntity<RestResult<List<MultimediaDTO>>> getByFamily(@RequestParam Long familyId) {
        List<MultimediaDTO> mediaList = multimediaService.getByFamily(familyId);
        return ResponseEntity.ok(RestResult.success(mediaList));
    }

    @GetMapping("/member")
    public ResponseEntity<RestResult<List<MultimediaDTO>>> getByMember(@RequestParam Long memberId) {
        List<MultimediaDTO> mediaList = multimediaService.getByMember(memberId);
        return ResponseEntity.ok(RestResult.success(mediaList));
    }
}