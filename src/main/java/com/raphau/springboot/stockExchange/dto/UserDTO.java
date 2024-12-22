package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private String name;
    private String surname;
    private String username;
    private String password;
    private String role;
    private BigDecimal money;
    private String email;
}
