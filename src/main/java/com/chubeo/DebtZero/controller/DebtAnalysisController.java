package com.chubeo.DebtZero.controller;

import com.chubeo.DebtZero.dto.response.*;
import com.chubeo.DebtZero.service.DebtAnalysisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/analysis")
public class DebtAnalysisController {
    DebtAnalysisService debtAnalysisService;

    @GetMapping("/overview")
    public ApiResponse<MonthlyOverviewResponse> getMonthlyOverview(){
        return ApiResponse.<MonthlyOverviewResponse>builder()
                .result(debtAnalysisService.getMonthlyOverview())
                .message("Get debt's monthly overview successfully")
                .build();
    }

    @GetMapping("/priority")
    public ApiResponse<DebtPriorityResponse> getPriorityOrder() {
        return ApiResponse.<DebtPriorityResponse>builder()
                .result(debtAnalysisService.getPriorityOrder())
                .message("Get debt's priority order successfully")
                .build();
    }

    @GetMapping("/warnings")
    public ApiResponse<List<WarningResponse>> generateWarnings(){
        return ApiResponse.<List<WarningResponse>>builder()
                .result(debtAnalysisService.generateWarnings())
                .message("Generate warning for debts successfully")
                .build();
    }

    @GetMapping("/bank-compare")
    public ApiResponse<BankRateComparisonResponse> compareWithBankRate(){
        return ApiResponse.<BankRateComparisonResponse>builder()
                .result(debtAnalysisService.compareWithBankRate())
                .message("Compare with bank rate")
                .build();
    }

}
