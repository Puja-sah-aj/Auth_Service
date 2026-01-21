
package com.online.shopping.auth.repository;

import com.online.shopping.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    //boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
    Optional<User> findByPhone(String phone);
}

