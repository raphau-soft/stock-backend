package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.service.ints.SellOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SellOfferRestController {

    @Autowired
    private SellOfferService sellOfferService;

    @PostMapping("/sellOffer")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addSellOffer(@RequestBody SellOfferDTO sellOfferDTO) throws InterruptedException {
        return ResponseEntity.ok(sellOfferService.addSellOffer(sellOfferDTO));
    }
}



























