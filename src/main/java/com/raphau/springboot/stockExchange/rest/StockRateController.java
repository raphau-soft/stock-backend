package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.service.ints.StockRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockRateController {
    @Autowired
    private StockRateService stockRateService;

    @GetMapping("/stockRates")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findStockRates(){
        return ResponseEntity.ok(stockRateService.findAllStockRates());
    }
}
















