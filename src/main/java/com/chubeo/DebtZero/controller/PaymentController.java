package com.chubeo.DebtZero.controller;

import com.chubeo.DebtZero.dto.request.RecordPaymentRequest;
import com.chubeo.DebtZero.dto.response.ApiResponse;
import com.chubeo.DebtZero.dto.response.PaymentHistoryResponse;
import com.chubeo.DebtZero.entity.Debt;
import com.chubeo.DebtZero.service.DebtService;
import com.chubeo.DebtZero.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;
    DebtService debtService;

    @PostMapping("/{debtId}/record")
    public ApiResponse<PaymentHistoryResponse> paymentRecord(@PathVariable UUID debtId, @Valid @RequestBody RecordPaymentRequest request){
        return ApiResponse.<PaymentHistoryResponse>builder()
                .result(paymentService.recordPayment(debtId, request))
                .message("Payment has been record successfully")
                .build();
    }

    @GetMapping("/history")
    public ApiResponse<List<PaymentHistoryResponse>> getAllPaymentsHistory(@PathVariable UUID debtId){
        return ApiResponse.<List<PaymentHistoryResponse>>builder()
                .result(paymentService.getPaymentHistory(debtId))
                .message("Get all payment's history successfully")
                .build();
    }


}
