package com.online.shopping.auth.repository;

import com.online.shopping.auth.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoginRepo extends JpaRepository<LoginEntity, UUID> {
    Optional<LoginEntity> findByPhoneNumber(String phoneNumber);
}
