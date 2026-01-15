package com.online.shopping.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String email;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    private String role = "USER";

    private LocalDateTime createdAt = LocalDateTime.now();
}
