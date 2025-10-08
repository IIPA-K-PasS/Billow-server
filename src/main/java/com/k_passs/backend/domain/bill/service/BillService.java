package com.k_passs.backend.domain.bill.service;

import com.k_passs.backend.domain.bill.dto.BillDto;
import com.k_passs.backend.domain.bill.entity.Bill;
import com.k_passs.backend.domain.bill.repository.BillRepository;
import com.k_passs.backend.domain.model.enums.BillType;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 사용
public class BillService {

    private final BillRepository billRepository;
    private final UserRepository userRepository; // 사용자 조회를 위해 추가

    /**
     * API 1: 메인 화면의 월별 고지서 요약을 조회합니다.
     */
    public BillDto.BillSummaryResponse getBillSummary(Long userId, int year, int month) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LocalDate thisMonth = LocalDate.of(year, month, 1);
        LocalDate lastMonth = thisMonth.minusMonths(1);

        // 1. 이번 달 총 요금 조회 (DB에서 직접 합산)
        BigDecimal totalAmountThisMonth = Optional.ofNullable(
                billRepository.sumTotalAmountByUserAndPeriodMonth(user, thisMonth)
        ).orElse(BigDecimal.ZERO);

        // 2. 지난달 총 요금 조회
        BigDecimal totalAmountLastMonth = Optional.ofNullable(
                billRepository.sumTotalAmountByUserAndPeriodMonth(user, lastMonth)
        ).orElse(BigDecimal.ZERO);

        // 3. 지난달 대비 차액 계산
        BigDecimal difference = totalAmountLastMonth.subtract(totalAmountThisMonth);

        return BillDto.BillSummaryResponse.builder()
                .year(year)
                .month(month)
                .totalAmount(totalAmountThisMonth)
                .difference(difference) // 양수: 절약, 음수: 추가 발생
                .build();
    }

    /**
     * API 2: 월별 상세 리포트를 조회합니다. (구현 예시)
     * 실제로는 additionalInfo, monthlyTrend 등을 채우는 로직이 더 복잡해집니다.
     */
    public BillDto.BillDetailReportResponse getBillDetailReport(Long userId, int year, int month, BillType category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LocalDate thisMonth = LocalDate.of(year, month, 1);

        // 이번 달 고지서 목록 전체를 가져온 뒤, 원하는 카테고리의 요금을 필터링
        List<Bill> billsThisMonth = billRepository.findByUserAndPeriodMonth(user, thisMonth);
        BigDecimal currentMonthFee = billsThisMonth.stream()
                .filter(bill -> bill.getCategory() == category)
                .findFirst()
                .map(Bill::getTotalAmount)
                .orElse(BigDecimal.ZERO);

        // ... 지난달 요금, 최근 6개월 추이, 추가 정보(AI 꿀팁 등) 조회 로직 추가 ...

        return BillDto.BillDetailReportResponse.builder()
                .category(category.name())
                .year(year)
                .month(month)
                .currentMonthFee(currentMonthFee)
                // .previousMonthFee(...)
                // .additionalInfo(...)
                // .monthlyTrend(...)
                .build();
    }
}
