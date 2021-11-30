package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.UserUpdDTO;
import com.raphau.springboot.stockExchange.service.ints.BuyOfferService;
import com.raphau.springboot.stockExchange.service.ints.SellOfferService;
import com.raphau.springboot.stockExchange.service.ints.StockService;
import com.raphau.springboot.stockExchange.service.ints.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private BuyOfferService buyOfferService;

    @Autowired
    private StockService stockService;

    @Autowired
    private SellOfferService sellOfferService;



    @GetMapping("/user")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> find(){
        return ResponseEntity.ok(userService.getUserDetails());
    }

    @GetMapping("/user/buyOffers")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findBuyOffers(){
        return ResponseEntity.ok(buyOfferService.getUserBuyOffers());
    }

    @GetMapping("/user/resources")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findResources(){
        return ResponseEntity.ok(stockService.findResources());
    }

    @GetMapping("/user/sellOffers")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findSellOffers() throws InterruptedException {
        return ResponseEntity.ok(sellOfferService.getUserSellOffers());
    }

    @DeleteMapping("user/sellOffers/{theId}")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteSellOffer(@PathVariable int theId){
        return ResponseEntity.ok(sellOfferService.deleteSellOffer(theId));
    }

    @DeleteMapping("user/buyOffers/{theId}")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteBuyOffer(@PathVariable int theId){
        return ResponseEntity.ok(buyOfferService.deleteBuyOffer(theId));
    }

    @PutMapping("user/login")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdDTO userUpdDTO){
        return ResponseEntity.ok(userService.updateUser(userUpdDTO));
    }

}




















