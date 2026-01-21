package com.online.shopping.auth.repository;


import com.online.shopping.auth.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepo extends JpaRepository<UserAddress, UUID> {
}
