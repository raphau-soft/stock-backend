package com.raphau.springboot.stockExchange.service;

import java.util.Map;

public interface TransactionService {

    Map<String, Object> findAllTransactions();

}
