package com.chubeo.DebtZero.dto.request;

import com.chubeo.DebtZero.enums.DebtCategory;
import com.chubeo.DebtZero.enums.InterestInputType;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateDebtRequest {
    @NotNull(message = "Debt category is required")
    private DebtCategory category;

    @NotBlank(message = "Debt name is required")
    private String name;

    @NotBlank(message = "Platform is required")
    private String platform;

    @NotNull(message = "Interest input type is required")
    private InterestInputType interestInputType;

    @Positive(message = "Interest rate must be greater than 0")
    private BigDecimal interestInputValue;

    @Min(value = 1, message = "Due day must be from 1 to 31")
    @Max(value = 31, message = "Due day must be from 1 to 31")
    private int dueDay;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @Positive(message = "Principal amount must be greater than 0")
    private BigDecimal principalAmount;

    @Positive(message = "Penalty rate must be greater than 0")
    private BigDecimal penaltyRate;

    @Min(value = 0, message = "Grace period days cannot be negative")
    private int gracePeriodDays;
}
