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
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;

}
