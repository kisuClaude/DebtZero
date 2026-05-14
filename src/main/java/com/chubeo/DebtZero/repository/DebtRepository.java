package com.chubeo.DebtZero.repository;

import com.chubeo.DebtZero.entity.Debt;
import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.enums.DebtStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DebtRepository extends JpaRepository<Debt, UUID> {
    List<Debt> findAllByUser(User user);
    Optional<Debt> findById(Debt debtId);

    List<Debt> findAllByUserAndStatus(User user, DebtStatus debtStatus);
}
