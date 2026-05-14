package com.chubeo.DebtZero.dto.response;

import com.chubeo.DebtZero.enums.WarningLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class MonthlyOverviewResponse {
    private BigDecimal totalMonthlyPayment;
    private BigDecimal monthlyIncome;
    private BigDecimal debtToIncomeRatio;
    private WarningLevel warningLevel;
}
