package com.k_passs.backend.domain.bill.repository;

import com.k_passs.backend.domain.bill.entity.Bill;
import com.k_passs.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    /**
     * 특정 유저의 특정 월 고지서 목록을 조회합니다.
     * (카테고리별로 데이터가 나뉘어 있으므로 List로 반환)
     * @param user 조회할 유저
     * @param periodMonth 조회할 연월 (YYYY-MM-01 형식)
     * @return 해당 월의 고지서 목록 (전기, 수도, 가스 등)
     */
    List<Bill> findByUserAndPeriodMonth(User user, LocalDate periodMonth);

    /**
     * 특정 유저의 특정 기간 동안의 고지서 목록을 조회합니다.
     * (최근 6개월 요금 추이 그래프에 사용)
     * @param user 조회할 유저
     * @param startDate 시작일 (e.g., 6개월 전 1일)
     * @param endDate 종료일 (e.g., 이번 달 1일)
     * @return 해당 기간의 모든 고지서 목록
     */
    List<Bill> findByUserAndPeriodMonthBetween(User user, LocalDate startDate, LocalDate endDate);

    /**
     * JPQL을 사용하여 특정 유저의 특정 월 고지서 총액을 계산합니다.
     * (여러 건의 고지서 금액을 DB에서 직접 합산하여 성능 최적화)
     * @param user 조회할 유저
     * @param periodMonth 조회할 연월
     * @return 해당 월의 고지서 총액 (합산된 금액)
     */
    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.user = :user AND b.periodMonth = :periodMonth")
    BigDecimal sumTotalAmountByUserAndPeriodMonth(@Param("user") User user, @Param("periodMonth") LocalDate periodMonth);
}