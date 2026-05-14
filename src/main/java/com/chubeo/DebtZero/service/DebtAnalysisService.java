package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.dto.response.*;
import com.chubeo.DebtZero.entity.Debt;
import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.enums.*;
import com.chubeo.DebtZero.repository.DebtRepository;
import com.chubeo.DebtZero.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DebtAnalysisService {
    SecurityUtils securityUtils;
    DebtRepository debtRepository;
    InterestCalculateService interestCalculateService;
    DebtService debtService;

    public MonthlyOverviewResponse getMonthlyOverview() {
        User user = securityUtils.getCurrentUser();

        List<Debt> activeDebts = debtRepository.findAllByUserAndStatus(user, DebtStatus.ACTIVE);

        BigDecimal totalMonthlyPayment = activeDebts.stream()
                .map(debt -> {
                    BigDecimal annualRate = interestCalculateService.convertToAnnualRate(debt.getInterestInputType(), debt.getInterestInputValue());

                    if (annualRate == null) {
                        return debt.getRemainingBalance();
                    }

                    return annualRate.divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP)
                            .multiply(debt.getRemainingBalance())
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal monthlyIncome = user.getMonthlyIncome();
        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            return new MonthlyOverviewResponse(totalMonthlyPayment, BigDecimal.ZERO, BigDecimal.ZERO, WarningLevel.SAFE);
        }

        BigDecimal debtToIncomeRatio = totalMonthlyPayment
                .divide(monthlyIncome, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));

        return new MonthlyOverviewResponse(
                totalMonthlyPayment,
                monthlyIncome,
                debtToIncomeRatio,
                getWarningLevel(debtToIncomeRatio)
        );
    }

    private WarningLevel getWarningLevel(BigDecimal ratio) {
        if (ratio.compareTo(new BigDecimal("40")) < 0) return WarningLevel.SAFE;
        if (ratio.compareTo(new BigDecimal("60")) < 0) return WarningLevel.CAUTION;
        return WarningLevel.DANGER;

    }

    public DebtPriorityResponse getPriorityOrder() {
        List<Debt> activeDebts = debtRepository
                .findAllByUserAndStatus(securityUtils.getCurrentUser(), DebtStatus.ACTIVE);

        List<DebtResponse> debtWithRates = activeDebts.stream()
                .map(debtService::enrichResponse)
                .collect(Collectors.toList());

        List<DebtResponse> avalanche = debtWithRates.stream()
                .sorted(Comparator.comparing(
                        DebtResponse::getAnnualInterestRate,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .collect(Collectors.toList());

        List<DebtResponse> snowball = debtWithRates.stream()
                .sorted(Comparator.comparing(DebtResponse::getRemainingBalance))
                .collect(Collectors.toList());

        PriorityRecommendation recommendation = debtWithRates.stream()
                .anyMatch(d -> d.getAnnualInterestRate() != null
                        && d.getAnnualInterestRate().compareTo(new BigDecimal("30")) > 0)
                ? PriorityRecommendation.AVALANCHE
                : PriorityRecommendation.SNOWBALL;

        return new DebtPriorityResponse(avalanche, snowball, recommendation);
    }

    public List<WarningResponse> generateWarnings() {
        User user = securityUtils.getCurrentUser();
        List<Debt> allDebts = debtRepository.findAllByUser(user);
        List<WarningResponse> warnings = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate in7Days = today.plusDays(7);

        for (Debt debt : allDebts) {
            if (debt.getStatus() == DebtStatus.OVERDUE) {
                warnings.add(new WarningResponse(
                        WarningType.OVERDUE,
                        Severity.HIGH,
                        "Khoản nợ \"" + debt.getName() + "\" đã trễ hạn thanh toán",
                        debt.getId()
                ));
            }
            if (debt.getStatus() == DebtStatus.ACTIVE) {
                try {
                    LocalDate dueDate = today.withDayOfMonth(debt.getDueDay());
                    if (!dueDate.isBefore(today) && !dueDate.isAfter(in7Days)) {
                        warnings.add(new WarningResponse(
                                WarningType.UPCOMING,
                                Severity.MEDIUM,
                                "Khoản nợ \"" + debt.getName() + "\" đến hạn ngày " + dueDate,
                                debt.getId()
                        ));
                    }
                } catch (DateTimeException ignored) {
                }
            }
        }
        BigDecimal monthlyIncome = user.getMonthlyIncome();
        if (monthlyIncome != null && monthlyIncome.compareTo(BigDecimal.ZERO) > 0) {
            MonthlyOverviewResponse overview = getMonthlyOverview();
            if (overview.getDebtToIncomeRatio().compareTo(new BigDecimal("40")) >= 0) {
                warnings.add(new WarningResponse(
                        WarningType.OVER_INCOME,
                        Severity.HIGH,
                        "Tổng nợ hàng tháng chiếm " + overview.getDebtToIncomeRatio() + "% thu nhập — vượt ngưỡng an toàn 40%",
                        null
                ));
            }
        }
        warnings.sort(Comparator.comparing(WarningResponse::getSeverity));

        return warnings;
    }

    public BankRateComparisonResponse compareWithBankRate(){
        User user = securityUtils.getCurrentUser();
        List<Debt> activeDebts = debtRepository.findAllByUserAndStatus(user, DebtStatus.ACTIVE);

        List<DebtExcessResponse> breakdown = new ArrayList<>();
        BigDecimal totoalExcessMonthly = BigDecimal.ZERO;

        for (Debt debt : activeDebts) {
            BigDecimal annualRate = interestCalculateService
                    .convertToAnnualRate(debt.getInterestInputType(), debt.getInterestInputValue());

            if (annualRate == null) continue;

            BigDecimal excessRate = interestCalculateService.compareWithBankRate(annualRate);

            if (excessRate.compareTo(BigDecimal.ZERO) == 0) continue;

            BigDecimal excessMonthly = excessRate
                    .divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP)
                    .multiply(debt.getRemainingBalance())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            BigDecimal excessAnnual = excessMonthly.multiply(new BigDecimal("12"));

            breakdown.add(new DebtExcessResponse(
                    debt.getId(),
                    debt.getName(),
                    annualRate,
                    excessMonthly,
                    excessAnnual,
                    interestCalculateService.getRiskLevel(annualRate)
            ));

            totoalExcessMonthly = totoalExcessMonthly.add(excessMonthly);
        }
        breakdown.sort(Comparator.comparing(DebtExcessResponse::getExcessMonthly).reversed());

        BigDecimal totalExcessAnnual = totoalExcessMonthly.multiply(new BigDecimal("12"));

        return new BankRateComparisonResponse(totoalExcessMonthly, totalExcessAnnual, breakdown);
    }
}

