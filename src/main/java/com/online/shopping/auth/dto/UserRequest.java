package com.online.shopping.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String name;
    private String email;
   // private String phoneNumber;
    private String dateOfBirth;
    private String gender;
    UserAddressRequest request;
}
