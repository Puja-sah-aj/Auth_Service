
package com.online.shopping.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserAddressResponse {
    private UUID id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}

