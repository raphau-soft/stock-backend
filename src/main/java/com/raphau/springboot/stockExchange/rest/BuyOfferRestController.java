package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.service.api.BuyOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BuyOfferRestController {

    @Autowired
    private BuyOfferService buyOfferService;

    @PostMapping("/buyOffer")
    public ResponseEntity<?> addOffer(@RequestBody BuyOfferDTO buyOfferDTO) throws InterruptedException {
        return ResponseEntity.ok(buyOfferService.addOffer(buyOfferDTO));
    }

}
