package com.raphau.springboot.stockExchange.service.api;

import com.raphau.springboot.stockExchange.entity.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> findAllTransactions();
}
