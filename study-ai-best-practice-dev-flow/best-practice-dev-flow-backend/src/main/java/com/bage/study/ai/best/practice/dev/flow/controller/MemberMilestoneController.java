package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.request.MemberMilestoneRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.MemberMilestoneDTO;
import com.bage.study.ai.best.practice.dev.flow.service.MemberMilestoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/milestones")
@RequiredArgsConstructor
@Slf4j
public class MemberMilestoneController {

    private final MemberMilestoneService milestoneService;

    @PostMapping
    public ResponseEntity<RestResult<MemberMilestoneDTO>> createMilestone(@Valid @RequestBody MemberMilestoneRequest request) {
        MemberMilestoneDTO milestone = milestoneService.createMilestone(request);
        return ResponseEntity.ok(RestResult.success("大事件创建成功", milestone));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResult<MemberMilestoneDTO>> updateMilestone(
            @PathVariable Long id,
            @Valid @RequestBody MemberMilestoneRequest request) {
        MemberMilestoneDTO milestone = milestoneService.updateMilestone(id, request);
        return ResponseEntity.ok(RestResult.success("大事件更新成功", milestone));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Void>> deleteMilestone(@PathVariable Long id) {
        milestoneService.deleteMilestone(id);
        return ResponseEntity.ok(RestResult.success(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResult<MemberMilestoneDTO>> getMilestoneById(@PathVariable Long id) {
        MemberMilestoneDTO milestone = milestoneService.getMilestoneById(id);
        return ResponseEntity.ok(RestResult.success(milestone));
    }

    @GetMapping("/member")
    public ResponseEntity<RestResult<List<MemberMilestoneDTO>>> getMilestonesByMember(@RequestParam Long memberId) {
        List<MemberMilestoneDTO> milestones = milestoneService.getMilestonesByMember(memberId);
        return ResponseEntity.ok(RestResult.success(milestones));
    }
}