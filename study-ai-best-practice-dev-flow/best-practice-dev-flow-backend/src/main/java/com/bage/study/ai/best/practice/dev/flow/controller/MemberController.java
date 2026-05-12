package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.request.MemberRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.MemberDTO;
import com.bage.study.ai.best.practice.dev.flow.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<RestResult<MemberDTO>> createMember(@Valid @RequestBody MemberRequest request) {
        MemberDTO member = memberService.createMember(request);
        return ResponseEntity.ok(RestResult.success("成员创建成功", member));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResult<MemberDTO>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {
        MemberDTO member = memberService.updateMember(id, request);
        return ResponseEntity.ok(RestResult.success("成员更新成功", member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(RestResult.success(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResult<MemberDTO>> getMemberById(@PathVariable Long id) {
        MemberDTO member = memberService.getMemberById(id);
        return ResponseEntity.ok(RestResult.success(member));
    }

    @GetMapping("/family")
    public ResponseEntity<RestResult<List<MemberDTO>>> getMembersByFamily(@RequestParam Long familyId) {
        List<MemberDTO> members = memberService.getMembersByFamily(familyId);
        return ResponseEntity.ok(RestResult.success(members));
    }

    @GetMapping("/search")
    public ResponseEntity<RestResult<List<MemberDTO>>> searchMembers(@RequestParam String keyword) {
        List<MemberDTO> members = memberService.searchMembers(keyword);
        return ResponseEntity.ok(RestResult.success(members));
    }
}