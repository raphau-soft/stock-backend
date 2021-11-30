package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.service.ints.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public class TransactionController implements Serializable {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findAllTransactions(){
        return ResponseEntity.ok(transactionService.findAllTransactions());
    }

}
