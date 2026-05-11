package com.chubeo.DebtZero.entity;

import com.chubeo.DebtZero.enums.DebtCategory;
import com.chubeo.DebtZero.enums.DebtStatus;
import com.chubeo.DebtZero.enums.InterestInputType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "debts")
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private DebtCategory category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "platform")
    private String platform;

    @Column(name = "principal_amount", nullable = false)
    private BigDecimal principalAmount;

    @Column(name = "remaining_balance", nullable = false)
    private BigDecimal remainingBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_input_type", nullable = false)
    private InterestInputType interestInputType;

    @Column(name = "interest_input_value")
    private BigDecimal interestInputValue;

    @Column(name = "penalty_rate")
    private BigDecimal penaltyRate;

    @Column(name = "grace_period_days")
    private int gracePeriodDays = 0;

    @Column(name = "due_day")
    private int dueDay;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DebtStatus status = DebtStatus.ACTIVE;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = OffsetDateTime.now();
    }
}
