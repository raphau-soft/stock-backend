package com.raphau.springboot.stockExchange.service.ints;

import java.util.Map;

public interface StockService {
    Map<String, Object> findResources();
    Map<String, Object> findResources(String username);
}
