package com.raphau.springboot.stockExchange.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequest {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
}
