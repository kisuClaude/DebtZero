package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.enums.InterestInputType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InterestCalculateService {
    public BigDecimal convertToAnnualRate(InterestInputType type, BigDecimal value){
        return switch (type){
            case DAILY_PER_MILLION -> convertDailyPerMilion(value);
            case MONTHLY_PERCENT -> convertMonthlyPercent(value);
            case ANNUAL_PERCENT -> value;
            case FIXED_MONTHLY -> null;
        };
    }


    public String getRiskLevel(BigDecimal annualRate){
        String level;
        BigDecimal fifteen = new BigDecimal("15");
        BigDecimal thirty = new BigDecimal("30");
        BigDecimal sixty = new BigDecimal("60");
        if(annualRate.compareTo(fifteen) < 0){
             level = "SAFE";
        } else if(annualRate.compareTo(thirty) < 0){
             level = "CAUTION";
        } else if(annualRate.compareTo(sixty) < 0) {
             level = "HIGH";
        } else  level = "DANGER";
        return level;
    }

    private BigDecimal convertDailyPerMilion(BigDecimal value) {
        return value
                .multiply(new BigDecimal("30"))
                .multiply(new BigDecimal("12"))
                .multiply(new BigDecimal("100"))
                .divide(new BigDecimal(1000000), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal convertMonthlyPercent(BigDecimal value){
        return value
                .multiply(new BigDecimal("12"));
    }

    public BigDecimal compareWithBankRate(BigDecimal annualRate) {
        if(annualRate == null) return null;
        BigDecimal bankRate = new BigDecimal("12");
        BigDecimal diff = annualRate.subtract(bankRate);

        if (diff.compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO;

        return diff;
    }
}
