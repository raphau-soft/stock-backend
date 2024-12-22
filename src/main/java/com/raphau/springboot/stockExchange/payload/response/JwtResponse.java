package com.raphau.springboot.stockExchange.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class JwtResponse {
    private String token;
    private List<String> roles;
}
