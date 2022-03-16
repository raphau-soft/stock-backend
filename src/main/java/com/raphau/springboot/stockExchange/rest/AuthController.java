package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.payload.request.LoginRequest;
import com.raphau.springboot.stockExchange.payload.request.SignupRequest;
import com.raphau.springboot.stockExchange.payload.response.JwtResponse;
import com.raphau.springboot.stockExchange.payload.response.MessageResponse;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.security.jwt.JwtUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = myUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        logger.info("Authenticated user - " + myUserDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt,
                myUserDetails.getId(),
                myUserDetails.getUsername(),
                myUserDetails.getEmail(),
                roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.ok(new MessageResponse("User already registered"));
        }

        User user = new User(0, signupRequest.getName(), signupRequest.getSurname(),
                signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()), new BigDecimal(1000000), signupRequest.getEmail(),
                "ROLE_USER");

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

}
























