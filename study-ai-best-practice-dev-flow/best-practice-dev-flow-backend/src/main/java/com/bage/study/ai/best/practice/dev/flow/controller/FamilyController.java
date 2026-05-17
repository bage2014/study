package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.request.FamilyRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.FamilyDTO;
import com.bage.study.ai.best.practice.dev.flow.service.FamilyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families")
@RequiredArgsConstructor
@Slf4j
public class FamilyController {

    private final FamilyService familyService;

    @PostMapping
    public ResponseEntity<RestResult<FamilyDTO>> createFamily(
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody FamilyRequest request) {
        FamilyDTO family = familyService.createFamily(userId, request);
        return ResponseEntity.ok(RestResult.success("家族创建成功", family));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResult<FamilyDTO>> updateFamily(
            @PathVariable Long id,
            @Valid @RequestBody FamilyRequest request) {
        FamilyDTO family = familyService.updateFamily(id, request);
        return ResponseEntity.ok(RestResult.success("家族更新成功", family));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Void>> deleteFamily(@PathVariable Long id) {
        familyService.deleteFamily(id);
        return ResponseEntity.ok(RestResult.success(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResult<FamilyDTO>> getFamilyById(@PathVariable Long id) {
        FamilyDTO family = familyService.getFamilyById(id);
        return ResponseEntity.ok(RestResult.success(family));
    }

    @GetMapping("/user")
    public ResponseEntity<RestResult<List<FamilyDTO>>> getFamiliesByUser(@RequestAttribute("userId") Long userId) {
        List<FamilyDTO> families = familyService.getFamiliesByUser(userId);
        return ResponseEntity.ok(RestResult.success(families));
    }

    @PostMapping("/{id}/transfer-admin")
    public ResponseEntity<RestResult<Void>> transferAdministrator(
            @PathVariable Long id,
            @RequestParam Long newAdminId) {
        familyService.transferAdministrator(id, newAdminId);
        return ResponseEntity.ok(RestResult.success("管理员转让成功", null));
    }
}