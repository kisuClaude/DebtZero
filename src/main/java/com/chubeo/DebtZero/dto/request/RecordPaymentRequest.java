package com.chubeo.DebtZero.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RecordPaymentRequest {
    @Positive(message = "Amount paid must be greater than 0")
    private BigDecimal amountPaid;

    private BigDecimal principalPaid;

    private BigDecimal interestPaid;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    private String note;
}
