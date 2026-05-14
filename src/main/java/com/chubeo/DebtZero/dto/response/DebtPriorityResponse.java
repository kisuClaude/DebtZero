package com.chubeo.DebtZero.dto.response;

import com.chubeo.DebtZero.enums.PriorityRecommendation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DebtPriorityResponse {
    private List<DebtResponse> avalancheOrder;
    private List<DebtResponse> snowballOrder;
    private PriorityRecommendation recommendation;
}
