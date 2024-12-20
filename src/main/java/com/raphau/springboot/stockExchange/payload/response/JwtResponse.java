package com.raphau.springboot.stockExchange.payload.response;

import java.util.List;

public class JwtResponse {
    int id;
    String jwt;
    String username;
    String email;
    List<String> roles;

    public JwtResponse(int id, String jwt, String username, String email, List<String> roles) {
        this.id = id;
        this.jwt = jwt;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
