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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @Mock
    private PaymentHistoryMapper paymentHistoryMapper;

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private DebtService debtService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldRecordPaymentSuccessfully() {

        UUID debtId = UUID.randomUUID();

        Debt debt = new Debt();
        debt.setStatus(DebtStatus.ACTIVE);

        RecordPaymentRequest request = new RecordPaymentRequest();
        request.setAmountPaid(BigDecimal.valueOf(500));
        request.setPaymentDate(LocalDate.now());

        PaymentHistory paymentHistory = new PaymentHistory();

        PaymentHistoryResponse response =
                new PaymentHistoryResponse();

        when(debtRepository.findById(debtId))
                .thenReturn(Optional.of(debt));

        when(paymentHistoryMapper.toPaymentHistory(request))
                .thenReturn(paymentHistory);

        when(paymentHistoryMapper.toPaymentHistoryResponse(paymentHistory))
                .thenReturn(response);

        PaymentHistoryResponse result =
                paymentService.recordPayment(debtId, request);

        assertNotNull(result);

        assertEquals(
                debt,
                paymentHistory.getDebt()
        );

        verify(paymentHistoryRepository)
                .save(paymentHistory);

        verify(debtService)
                .updateRemainingBalance(
                        debtId,
                        request.getAmountPaid()
                );
    }

    @Test
    void shouldThrowExceptionWhenDebtNotFound() {

        UUID debtId = UUID.randomUUID();

        RecordPaymentRequest request =
                new RecordPaymentRequest();

        when(debtRepository.findById(debtId))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(
                AppException.class,
                () -> paymentService.recordPayment(debtId, request)
        );

        assertEquals(
                ErrorCode.DEBT_NOT_FOUND,
                exception.getErrorCode()
        );
    }

    @Test
    void shouldMarkPaymentAsLateWhenPayAfterDueDay() {
        UUID debtId = UUID.randomUUID();

        Debt debt = new Debt();
        debt.setStatus(DebtStatus.ACTIVE);
        debt.setDueDay(15);

        RecordPaymentRequest request = new RecordPaymentRequest();
        request.setAmountPaid(BigDecimal.valueOf(500));
        request.setPaymentDate(LocalDate.of(2026, 5, 18));

        PaymentHistory paymentHistory = new PaymentHistory();
        PaymentHistoryResponse response = new PaymentHistoryResponse();

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));
        when(paymentHistoryMapper.toPaymentHistory(request)).thenReturn(paymentHistory);
        when(paymentHistoryMapper.toPaymentHistoryResponse(paymentHistory)).thenReturn(response);

        paymentService.recordPayment(debtId, request);

        assertTrue(paymentHistory.isLate());
    }

    @Test
    void shouldThrowExceptionWhenDebtAlreadyPaid() {

        UUID debtId = UUID.randomUUID();

        Debt debt = new Debt();
        debt.setStatus(DebtStatus.PAID);

        RecordPaymentRequest request =
                new RecordPaymentRequest();

        when(debtRepository.findById(debtId))
                .thenReturn(Optional.of(debt));
        AppException exception = assertThrows(
                AppException.class,
                () -> paymentService.recordPayment(debtId, request)
        );

        assertEquals(
                ErrorCode.DEBT_HAS_PAID,
                exception.getErrorCode()
        );
    }
}