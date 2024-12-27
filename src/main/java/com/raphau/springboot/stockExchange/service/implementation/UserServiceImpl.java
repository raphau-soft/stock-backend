package com.raphau.springboot.stockExchange.service.implementation;


import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.UserAlreadyExistsException;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.payload.request.SignupRequest;
import com.raphau.springboot.stockExchange.service.api.UserService;
import com.raphau.springboot.stockExchange.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    public void createUser(SignupRequest signupRequest) {
        validateUserExistence(signupRequest.getUsername());

        User user = buildNewUser(signupRequest);

        userRepository.save(user);
    }

    private void validateUserExistence(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User with username " + username + " already exists");
        }
    }

    private User buildNewUser(SignupRequest signupRequest) {
        return User.builder()
                .name(signupRequest.getName())
                .surname(signupRequest.getSurname())
                .username(signupRequest.getUsername())
                .password(encoder.encode(signupRequest.getPassword()))
                .money(BigDecimal.valueOf(1000000))
                .email(signupRequest.getEmail())
                .role("ROLE_USER")
                .build();
    }

    public User getUserDetails() {
        String username = AuthUtils.getAuthenticatedUsername();
        User user = findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));

        user.setPassword(null);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
