package com.raphau.springboot.stockExchange.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JwtResponse {

    @JsonProperty
    String jwt;
    @JsonProperty
    int id;
    @JsonProperty
    String username;
    @JsonProperty
    String email;
    @JsonProperty
    List<String> roles;

    public JwtResponse(String jwt, int id, String username, String email, List<String> roles) {
        this.jwt = jwt;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
