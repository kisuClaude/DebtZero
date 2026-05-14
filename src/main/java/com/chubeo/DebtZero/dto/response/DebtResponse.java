package com.chubeo.DebtZero.dto.response;
import com.chubeo.DebtZero.enums.DebtCategory;
import com.chubeo.DebtZero.enums.RiskLevel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class DebtResponse {
    private UUID id;
    private DebtCategory category;
    private String name;
    private String platform;
    private int dueDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private String note;
    private BigDecimal principalAmount;
    private BigDecimal remainingBalance;
    private int gracePeriodDays;
    private BigDecimal annualInterestRate;
    private RiskLevel riskLevel;
    private BigDecimal excessRate;
}
