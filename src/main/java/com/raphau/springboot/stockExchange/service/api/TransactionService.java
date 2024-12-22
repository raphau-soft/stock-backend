package com.raphau.springboot.stockExchange.service.api;

import java.util.Map;

public interface TransactionService {

    Map<String, Object> findAllTransactions();

}
