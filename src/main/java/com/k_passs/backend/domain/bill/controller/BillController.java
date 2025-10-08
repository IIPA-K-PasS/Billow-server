package com.k_passs.backend.domain.bill.controller;

import com.k_passs.backend.domain.bill.dto.BillDto;
import com.k_passs.backend.domain.bill.dto.BillOcrResponseDto;
import com.k_passs.backend.domain.bill.service.BillOcrService;
import com.k_passs.backend.domain.bill.service.BillService;
import com.k_passs.backend.domain.model.enums.BillType;
import com.k_passs.backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "고지서 API", description = "월별 고지서 조회 및 OCR 분석 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;
    private final BillOcrService billOcrService;

    @GetMapping("/summary")
    @Operation(summary = "메인 화면 월별 고지서 요약 조회")
    public ResponseEntity<BillDto.BillSummaryResponse> getBillSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {

        Long userId = userDetails.getUserId();
        BillDto.BillSummaryResponse response = billService.getBillSummary(userId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/report/detail")
    @Operation(summary = "월별 상세 리포트 조회")
    public ResponseEntity<BillDto.BillDetailReportResponse> getBillDetailReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("category") BillType category) {

        Long userId = userDetails.getUserId();
        BillDto.BillDetailReportResponse response = billService.getBillDetailReport(userId, year, month, category);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "고지서 이미지 OCR 분석 요청")
    public ResponseEntity<BillOcrResponseDto> analyzeBill(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("billType") BillType billType,
            @RequestParam("billImage") MultipartFile billImage) {

        Long userId = userDetails.getUserId();
        BillOcrResponseDto result = billOcrService.analyzeBill(userId, billType, billImage);
        return ResponseEntity.ok(result);
    }
}