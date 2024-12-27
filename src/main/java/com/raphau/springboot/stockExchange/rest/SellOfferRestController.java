package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.service.api.SellOfferService;
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
    public ResponseEntity<?> addSellOffer(@RequestBody SellOfferDTO sellOfferDTO) throws InterruptedException {
        sellOfferService.addSellOffer(sellOfferDTO);
        return ResponseEntity.ok().build();
    }
}



























