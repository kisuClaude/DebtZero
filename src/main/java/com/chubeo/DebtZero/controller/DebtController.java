package com.chubeo.DebtZero.controller;

import com.chubeo.DebtZero.dto.request.CreateDebtRequest;
import com.chubeo.DebtZero.dto.response.ApiResponse;
import com.chubeo.DebtZero.dto.response.DebtResponse;
import com.chubeo.DebtZero.service.DebtService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/debt")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DebtController {
    DebtService debtService;

    @PostMapping("/create")
    public ApiResponse<DebtResponse> createDebt(@Valid @RequestBody CreateDebtRequest request){
        return ApiResponse.<DebtResponse>builder()
                .result(debtService.createDebt(request))
                .message("Create debt successfully")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<DebtResponse>> getDebtsByUser(){
        return ApiResponse.<List<DebtResponse>>builder()
                .result(debtService.getDebtsByUser())
                .message("Get all debts from user")
                .build();
    }

    @PatchMapping("/{debtId}/balance")
    public ApiResponse<DebtResponse> updateBalance(@PathVariable UUID debtId, @RequestParam BigDecimal amount){
        return ApiResponse.<DebtResponse>builder()
                .result(debtService.updateRemainingBalance(debtId, amount))
                .message("Update balance successfully")
                .build();
    }

    @DeleteMapping("/{debtId}")
    public ApiResponse<Void> deleteDebt(@PathVariable UUID debtId) {
        debtService.deleteDebt(debtId);
        return ApiResponse.<Void>builder()
                .message("Delete debt successfully")
                .build();
    }

    @GetMapping("/{debtId}")
    public ApiResponse<DebtResponse> getDebtById(@PathVariable UUID debtId){
        return ApiResponse.<DebtResponse>builder()
                .result(debtService.getDebtById(debtId))
                .message("Get debt successfully")
                .build();
    }

    @GetMapping("/upcoming")
    public ApiResponse<List<DebtResponse>> getUpComingPayment(){
        return ApiResponse.<List<DebtResponse>>builder()
                .result(debtService.getUpcomingPayments())
                .message("All upcoming payment for debt")
                .build();
    }
}
