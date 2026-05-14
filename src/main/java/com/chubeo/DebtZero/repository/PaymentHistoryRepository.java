package com.chubeo.DebtZero.repository;

import com.chubeo.DebtZero.entity.Debt;
import com.chubeo.DebtZero.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, UUID> {
    Optional<PaymentHistory> findAllById(UUID debtId);
}
