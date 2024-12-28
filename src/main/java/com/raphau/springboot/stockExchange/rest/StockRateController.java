package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.service.StockRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockRateController {
    @Autowired
    private StockRateService stockRateService;

    @GetMapping("/stockRates")
    public ResponseEntity<?> findStockRates() {
        return ResponseEntity.ok(stockRateService.findAllStockRates());
    }
}
















