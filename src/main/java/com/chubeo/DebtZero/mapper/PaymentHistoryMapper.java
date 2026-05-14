package com.chubeo.DebtZero.mapper;

import com.chubeo.DebtZero.dto.request.RecordPaymentRequest;
import com.chubeo.DebtZero.dto.response.PaymentHistoryResponse;
import com.chubeo.DebtZero.entity.PaymentHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentHistoryMapper {
    PaymentHistory toPaymentHistory(RecordPaymentRequest request);

    @Mapping(target = "debtId", source = "debt.id")
    PaymentHistoryResponse toPaymentHistoryResponse(PaymentHistory paymentHistory);
}
