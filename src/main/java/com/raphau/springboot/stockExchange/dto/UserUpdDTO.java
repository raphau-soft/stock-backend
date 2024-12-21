package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdDTO {
    private String username;
    private String password;
}
