package com.raphau.springboot.stockExchange.service.api;

import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.payload.request.SignupRequest;

import java.util.Optional;

public interface UserService {
    User getUserDetails();
    Optional<User> findByUsername(String username);
    void createUser(SignupRequest signupRequest);
}
