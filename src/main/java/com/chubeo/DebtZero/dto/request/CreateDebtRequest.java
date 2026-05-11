package com.chubeo.DebtZero.dto.request;

import com.chubeo.DebtZero.enums.DebtCategory;
import com.chubeo.DebtZero.enums.InterestInputType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateDebtRequest {
    private DebtCategory category;
    private String name;
    private String platform;
    private InterestInputType interestInputType;
    private BigDecimal interestInputValue;
    private int dueDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal principalAmount;
    private BigDecimal penaltyRate;
    private int gracePeriodDays;
    private String note;
}
