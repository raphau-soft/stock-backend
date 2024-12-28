package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.service.BuyOfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BuyOfferRestController {

    @Autowired
    private BuyOfferService buyOfferService;

    @PostMapping("/buyOffer")
    public ResponseEntity<?> addOffer(@Valid @RequestBody BuyOfferDTO buyOfferDTO) {
        buyOfferService.addOffer(buyOfferDTO);
        return ResponseEntity.ok().build();
    }

}
