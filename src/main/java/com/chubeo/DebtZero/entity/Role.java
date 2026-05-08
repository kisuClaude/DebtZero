package com.chubeo.DebtZero.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Table(name = "roles")
@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
