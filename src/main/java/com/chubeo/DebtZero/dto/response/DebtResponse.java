package com.chubeo.DebtZero.dto.response;
import com.chubeo.DebtZero.enums.DebtCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DebtResponse {
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
    private String riskLevel;
    private BigDecimal excessRate;
}
