package com.raphau.springboot.stockExchange.service.ints;

import java.util.Map;

public interface TransactionService {

    Map<String, Object> findAllTransactions();

}
