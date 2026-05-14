package com.chubeo.DebtZero.dto.response;

import com.chubeo.DebtZero.entity.Debt;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PaymentHistoryResponse {
    private UUID id;
    private UUID debtId;
    private BigDecimal amountPaid;
    private BigDecimal principalPaid;
    private BigDecimal interestPaid;
    private LocalDate paymentDate;
    private boolean isLate = false;
    private String note;
}
