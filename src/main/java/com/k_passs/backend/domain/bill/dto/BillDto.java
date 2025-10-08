package com.k_passs.backend.domain.bill.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillDto {

    // API 1: 메인 화면 요약 응답
    @Getter
    @Builder
    public static class BillSummaryResponse {
        private int year;
        private int month;
        private BigDecimal totalAmount; // 이번 달 총 요금
        private BigDecimal difference;  // 지난달 대비 절약 금액
    }

    // API 2: 상세 리포트 응답
    @Getter
    @Builder
    public static class BillDetailReportResponse {
        private String category;        // 카테고리 (ELECTRICITY, WATER, GAS)
        private int year;
        private int month;
        private BigDecimal currentMonthFee; // 이번 달 요금
        private BigDecimal previousMonthFee;// 지난달 요금
        private Object additionalInfo;      // 추가 정보 (누진 단계, 안전 점검 등)
        private List<MonthlyTrend> monthlyTrend; // 최근 6개월 요금 추이
    }

    // 상세 리포트 내 월별 추이 데이터
    @Getter
    @Builder
    public static class MonthlyTrend {
        private int month;
        private BigDecimal fee;
    }
}
