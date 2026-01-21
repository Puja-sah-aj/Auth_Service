package com.online.shopping.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressRequest {
    private String addressLine1;
    private String city;
    private String state;
    private Integer postalCode;
    private String country;

}
