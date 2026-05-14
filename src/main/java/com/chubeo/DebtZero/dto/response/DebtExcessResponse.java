package com.chubeo.DebtZero.dto.response;

import com.chubeo.DebtZero.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
public class DebtExcessResponse {
    private UUID debtid;
    private String debtName;
    private BigDecimal annualInterestRate;
    private BigDecimal excessMonthly;
    private BigDecimal excessAnnual;
    private RiskLevel riskLevel;
}
