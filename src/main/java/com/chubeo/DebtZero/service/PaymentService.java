package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.dto.request.RecordPaymentRequest;
import com.chubeo.DebtZero.dto.response.PaymentHistoryResponse;
import com.chubeo.DebtZero.entity.Debt;
import com.chubeo.DebtZero.entity.PaymentHistory;
import com.chubeo.DebtZero.enums.DebtStatus;
import com.chubeo.DebtZero.exception.AppException;
import com.chubeo.DebtZero.exception.ErrorCode;
import com.chubeo.DebtZero.mapper.PaymentHistoryMapper;
import com.chubeo.DebtZero.repository.DebtRepository;
import com.chubeo.DebtZero.repository.PaymentHistoryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    DebtRepository debtRepository;
    PaymentHistoryMapper paymentHistoryMapper;
    PaymentHistoryRepository paymentHistoryRepository;
    DebtService debtService;

    public PaymentHistoryResponse recordPayment(UUID debtId, RecordPaymentRequest request){
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new AppException(ErrorCode.DEBT_NOT_FOUND));

        if (debt.getStatus().equals(DebtStatus.PAID))
            throw new AppException(ErrorCode.DEBT_HAS_PAID);

        PaymentHistory payment = paymentHistoryMapper.toPaymentHistory(request);
        payment.setDebt(debt);
        payment.setLate(isLateCheck(request, debt));

        paymentHistoryRepository.save(payment);
        debtService.updateRemainingBalance(debtId, request.getAmountPaid());

        return paymentHistoryMapper.toPaymentHistoryResponse(payment);
    }

    public List<PaymentHistoryResponse> getPaymentHistory(UUID debtId){
        debtRepository.findById(debtId)
                .orElseThrow(() -> new AppException(ErrorCode.DEBT_NOT_FOUND));

        return paymentHistoryRepository.findAllById(debtId)
                .stream().map(paymentHistoryMapper::toPaymentHistoryResponse)
                .collect(Collectors.toList());
    }


    private boolean isLateCheck(RecordPaymentRequest request, Debt debt){
        return request.getPaymentDate().getDayOfMonth() > debt.getDueDay();
    }
}
