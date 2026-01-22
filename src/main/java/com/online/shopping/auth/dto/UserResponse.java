package com.online.shopping.auth.dto;

import com.online.shopping.auth.entity.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;

    private List<UserAddressResponse> userAddress;

}
