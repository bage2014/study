package com.bage.study.ai.controller;

import com.bage.study.ai.dto.request.ProductSearchRequest;
import com.bage.study.ai.dto.response.CompareResultResponse;
import com.bage.study.ai.service.PriceCompareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/compare")
@RequiredArgsConstructor
public class PriceCompareController {

    private final PriceCompareService priceCompareService;

    @PostMapping("/search")
    public ResponseEntity<CompareResultResponse> searchAndCompare(@Valid @RequestBody ProductSearchRequest request) {
        log.info("收到价格比较请求: {}", request.getProductName());
        CompareResultResponse result = priceCompareService.comparePrices(
                request.getProductName(), 
                request.getAddressId()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<CompareResultResponse> searchAndCompareGet(
            @RequestParam String productName,
            @RequestParam(required = false) String addressId) {
        log.info("收到价格比较请求(GET): {}", productName);
        CompareResultResponse result = priceCompareService.comparePrices(productName, addressId);
        return ResponseEntity.ok(result);
    }
}