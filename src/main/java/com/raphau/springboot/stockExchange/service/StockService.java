package com.raphau.springboot.stockExchange.service;

import java.util.Map;

public interface StockService {
    Map<String, Object> findResources();
    Map<String, Object> findResources(String username);
}
