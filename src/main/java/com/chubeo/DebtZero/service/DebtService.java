package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.dto.request.CreateDebtRequest;
import com.chubeo.DebtZero.dto.response.DebtResponse;
import com.chubeo.DebtZero.entity.Debt;
import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.enums.DebtStatus;
import com.chubeo.DebtZero.exception.AppException;
import com.chubeo.DebtZero.exception.ErrorCode;
import com.chubeo.DebtZero.mapper.DebtMapper;
import com.chubeo.DebtZero.repository.DebtRepository;
import com.chubeo.DebtZero.repository.UserRepository;
import com.chubeo.DebtZero.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DebtService {
    UserRepository userRepository;
    DebtRepository debtRepository;
    DebtMapper debtMapper;
    InterestCalculateService interestCalculateService;
    SecurityUtils securityUtils;

    public DebtResponse createDebt(CreateDebtRequest request){
        User user = securityUtils.getCurrentUser();

        Debt debt = debtMapper.toDebt(request);
        debt.setUser(user);
        debt.setRemainingBalance(request.getPrincipalAmount());

        debtRepository.save(debt);

        return enrichResponse(debt);
    }

    public List<DebtResponse> getDebtsByUser(){
        User user = securityUtils.getCurrentUser();
        return debtRepository.findAllByUser(user)
                .stream()
                .map(this::enrichResponse)
                .collect(Collectors.toList());
    }

    public DebtResponse getDebtById(UUID debtId){
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new AppException(ErrorCode.DEBT_NOT_FOUND));
        return enrichResponse(debt);
    }

    public void deleteDebt(UUID debtId){
        debtRepository.findById(debtId)
                .orElseThrow(() -> new AppException(ErrorCode.DEBT_NOT_FOUND));
        debtRepository.deleteById(debtId);
    }

    public DebtResponse updateRemainingBalance(UUID debtId, BigDecimal amount){
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new AppException(ErrorCode.DEBT_NOT_FOUND));

        BigDecimal newBalance = debt.getRemainingBalance().subtract(amount);

        if(newBalance.compareTo(BigDecimal.ZERO) <= 0){
            debt.setRemainingBalance(BigDecimal.ZERO);
            debt.setStatus(DebtStatus.PAID);
        } else debt.setRemainingBalance(newBalance);

        debtRepository.save(debt);
        return enrichResponse(debt);
    }


    private DebtResponse enrichResponse(Debt debt){
        DebtResponse response = debtMapper.toDebtResponse(debt);

        BigDecimal annualRate = interestCalculateService
                .convertToAnnualRate(debt.getInterestInputType(), debt.getInterestInputValue());

        response.setAnnualInterestRate(annualRate);
        response.setRiskLevel(interestCalculateService.getRiskLevel(annualRate));
        response.setExcessRate(interestCalculateService.compareWithBankRate(annualRate));

        return response;
    }


}
