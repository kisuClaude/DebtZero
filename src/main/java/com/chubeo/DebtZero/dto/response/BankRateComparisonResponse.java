package com.chubeo.DebtZero.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
public class BankRateComparisonResponse {
    private BigDecimal totalExcessMonthly;
    private BigDecimal totalExcessAnnual;
    List<DebtExcessResponse> breakdown;
}
