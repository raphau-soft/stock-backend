package com.raphau.springboot.stockExchange.service.implementation;


import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.dto.UserUpdDTO;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.payload.request.SignupRequest;
import com.raphau.springboot.stockExchange.payload.response.MessageResponse;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    public void createUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("User exists");
        }

        User user = new User(0, signupRequest.getName(), signupRequest.getSurname(),
                signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()), new BigDecimal(1000000), signupRequest.getEmail(),
                "ROLE_USER");

        userRepository.save(user);
    }

    public Map<String, Object> getUserDetails(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        if (!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        User user = userOpt.get();
        user.setPassword(null);

        Map<String, Object> objects = new HashMap<>();
        objects.put("user", user);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return objects;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public TestDetailsDTO updateUser(UserUpdDTO userUpdDTO) {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        User user = userOpt.get();

        user.setUsername(userUpdDTO.getUsername());
        user.setPassword(userUpdDTO.getPassword());

        timeBase = System.currentTimeMillis();
        userRepository.save(user);
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase + testDetailsDTO.getDatabaseTime());

        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }
}
