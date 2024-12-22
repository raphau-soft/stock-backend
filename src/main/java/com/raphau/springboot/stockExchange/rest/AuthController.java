package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.payload.request.LoginRequest;
import com.raphau.springboot.stockExchange.payload.request.SignupRequest;
import com.raphau.springboot.stockExchange.payload.response.JwtResponse;
import com.raphau.springboot.stockExchange.payload.response.MessageResponse;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.security.jwt.JwtUtils;
import com.raphau.springboot.stockExchange.service.api.CompanyService;
import com.raphau.springboot.stockExchange.service.api.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = myUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(token, roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

}
























