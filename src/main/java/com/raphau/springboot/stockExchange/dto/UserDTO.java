package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {
    private String username;
    private String name;
    private String surname;
    private String email;
    private String role;
    private BigDecimal money;
}
