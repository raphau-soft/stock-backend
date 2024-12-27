package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.service.api.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<?> findAllCompanies() {
        return ResponseEntity.ok(companyService.findAllCompanies());
    }

    @PostMapping("/company")
    public ResponseEntity<?> addCompany(@RequestBody CompanyDTO companyDTO) {
        companyService.addCompany(companyDTO);
        return ResponseEntity.ok().build();
    }

}
