package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.UserUpdDTO;
import com.raphau.springboot.stockExchange.service.api.BuyOfferService;
import com.raphau.springboot.stockExchange.service.api.SellOfferService;
import com.raphau.springboot.stockExchange.service.api.StockService;
import com.raphau.springboot.stockExchange.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private BuyOfferService buyOfferService;

    @Autowired
    private StockService stockService;

    @Autowired
    private SellOfferService sellOfferService;

    @GetMapping()
    public ResponseEntity<?> find() {
        return ResponseEntity.ok(userService.getUserDetails());
    }

    @GetMapping("/buyOffers")
    public ResponseEntity<?> findBuyOffers() {
        return ResponseEntity.ok(buyOfferService.getUserBuyOffers());
    }

    @GetMapping("/resources")
    public ResponseEntity<?> findResources() {
        return ResponseEntity.ok(stockService.findResources());
    }

    @GetMapping("/sellOffers")
    public ResponseEntity<?> findSellOffers() throws InterruptedException {
        return ResponseEntity.ok(sellOfferService.getUserSellOffers());
    }

    @DeleteMapping("/sellOffers/{id}")
    public ResponseEntity<?> deleteSellOffer(@PathVariable(name = "id") int id) {
        return ResponseEntity.ok(sellOfferService.deleteSellOffer(id));
    }

    @DeleteMapping("/buyOffers/{id}")
    public ResponseEntity<?> deleteBuyOffer(@PathVariable(name = "id") int id) {
        return ResponseEntity.ok(buyOfferService.deleteBuyOffer(id));
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody UserUpdDTO userUpdDTO) {
        return ResponseEntity.ok(userService.updateUser(userUpdDTO));
    }

}




















