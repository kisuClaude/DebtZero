package com.chubeo.DebtZero.mapper;

import com.chubeo.DebtZero.dto.request.CreateDebtRequest;
import com.chubeo.DebtZero.dto.response.DebtResponse;
import com.chubeo.DebtZero.entity.Debt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DebtMapper {
    Debt toDebt(CreateDebtRequest request);

    @Mapping(target = "annualInterestRate", ignore = true)
    @Mapping(target = "riskLevel", ignore = true)
    @Mapping(target = "excessRate", ignore = true)
    DebtResponse toDebtResponse(Debt debt);
}
