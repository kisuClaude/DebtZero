package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.dto.request.CreateDebtRequest;
import com.chubeo.DebtZero.dto.response.DebtResponse;
import com.chubeo.DebtZero.entity.Debt;
import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.enums.DebtStatus;
import com.chubeo.DebtZero.enums.RiskLevel;
import com.chubeo.DebtZero.mapper.DebtMapper;
import com.chubeo.DebtZero.repository.DebtRepository;
import com.chubeo.DebtZero.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DebtServiceTest {
    @Mock
    private DebtRepository debtRepository;

    @Mock
    private DebtMapper debtMapper;

    @Mock
    private InterestCalculateService interestCalculateService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private DebtService debtService;

    @Test
    void shouldCreateDebtSuccessfully() {

        // Arrange
        User user = new User();
        user.setUsername("anh");

        CreateDebtRequest request = new CreateDebtRequest();
        request.setPrincipalAmount(BigDecimal.valueOf(1000));

        Debt debt = new Debt();
        debt.setPrincipalAmount(BigDecimal.valueOf(1000));

        DebtResponse debtResponse = new DebtResponse();

        when(securityUtils.getCurrentUser())
                .thenReturn(user);

        when(debtMapper.toDebt(request))
                .thenReturn(debt);

        Debt savedDebt = new Debt();
        savedDebt.setId(UUID.randomUUID());
        savedDebt.setPrincipalAmount(BigDecimal.valueOf(1000));
        when(debtRepository.save(any())).thenReturn(savedDebt);
        when(debtMapper.toDebtResponse(savedDebt)).thenReturn(debtResponse);

        when(interestCalculateService.convertToAnnualRate(any(), any()))
                .thenReturn(BigDecimal.valueOf(12));

        when(interestCalculateService.getRiskLevel(any()))
                .thenReturn(RiskLevel.CAUTION);

        when(interestCalculateService.compareWithBankRate(any()))
                .thenReturn(BigDecimal.valueOf(5));

        // Act
        DebtResponse response = debtService.createDebt(request);

        // Assert
        assertNotNull(response);

        assertEquals(
                BigDecimal.valueOf(1000),
                debt.getRemainingBalance()
        );

        assertEquals(user, debt.getUser());

        // Verify
        verify(debtRepository).save(debt);
    }

    @Test
    void shouldMarkDebtAsPaidWhenBalanceBecomesZero() {

        // Arrange
        UUID debtId = UUID.randomUUID();

        Debt debt = new Debt();
        debt.setRemainingBalance(BigDecimal.valueOf(1000));
        debt.setStatus(DebtStatus.ACTIVE);

        DebtResponse debtResponse = new DebtResponse();

        when(debtRepository.findById(debtId))
                .thenReturn(Optional.of(debt));

        when(debtRepository.save(any())).thenReturn(debt);
        when(debtMapper.toDebtResponse(debt)).thenReturn(debtResponse);

        when(interestCalculateService.convertToAnnualRate(any(), any()))
                .thenReturn(BigDecimal.valueOf(12));

        when(interestCalculateService.getRiskLevel(any()))
                .thenReturn(RiskLevel.CAUTION);

        when(interestCalculateService.compareWithBankRate(any()))
                .thenReturn(BigDecimal.valueOf(5));

        // Act
        DebtResponse response =
                debtService.updateRemainingBalance(
                        debtId,
                        BigDecimal.valueOf(1000)
                );

        // Assert
        assertEquals(
                BigDecimal.ZERO,
                debt.getRemainingBalance()
        );

        assertEquals(
                DebtStatus.PAID,
                debt.getStatus()
        );

        assertNotNull(response);

        // Verify
        verify(debtRepository).save(debt);
    }

    @Test
    void shouldKeepDebtActiveWhenBalanceStillPositive() {

        // Arrange
        UUID debtId = UUID.randomUUID();

        Debt debt = new Debt();
        debt.setRemainingBalance(BigDecimal.valueOf(1000));
        debt.setStatus(DebtStatus.ACTIVE);

        DebtResponse debtResponse = new DebtResponse();

        when(debtRepository.findById(debtId))
                .thenReturn(Optional.of(debt));

        when(debtRepository.save(any())).thenReturn(debt);
        when(debtMapper.toDebtResponse(debt)).thenReturn(debtResponse);

        when(interestCalculateService.convertToAnnualRate(any(), any()))
                .thenReturn(BigDecimal.valueOf(12));

        when(interestCalculateService.getRiskLevel(any()))
                .thenReturn(RiskLevel.CAUTION);

        when(interestCalculateService.compareWithBankRate(any()))
                .thenReturn(BigDecimal.valueOf(5));

        // Act
        debtService.updateRemainingBalance(
                debtId,
                BigDecimal.valueOf(300)
        );

        // Assert
        assertEquals(
                BigDecimal.valueOf(700),
                debt.getRemainingBalance()
        );

        assertEquals(
                DebtStatus.ACTIVE,
                debt.getStatus()
        );

        // Verify
        verify(debtRepository).save(debt);
    }
}
