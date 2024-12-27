package com.raphau.springboot.stockExchange.service.api;

import com.raphau.springboot.stockExchange.entity.StockRate;

import java.util.List;

public interface StockRateService {
    List<StockRate> findAllStockRates();
}
