package com.chubeo.DebtZero.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@EnableJpaAuditing
@Table(name = "users")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "last_name", length = 20)
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true, length = 50)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = OffsetDateTime.now();
    }
}


