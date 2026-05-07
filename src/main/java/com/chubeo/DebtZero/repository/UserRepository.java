package com.chubeo.DebtZero.repository;

import com.chubeo.DebtZero.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    boolean existsByphoneNumber(String phoneNumber);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
