package com.raphau.springboot.stockExchange.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class JwtResponse {
    private int id;
    private String jwt;
    private String username;
    private String email;
    private List<String> roles;
}
