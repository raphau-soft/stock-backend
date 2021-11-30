package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.service.ints.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
public class CompanyRestController implements Serializable {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/companies")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findAllCompanies(){
        return ResponseEntity.ok(companyService.findAllCompanies());
    }

    @PostMapping("/company")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addCompany(@RequestBody CompanyDTO companyDTO){
        return ResponseEntity.ok(companyService.addCompany(companyDTO));
    }

}
