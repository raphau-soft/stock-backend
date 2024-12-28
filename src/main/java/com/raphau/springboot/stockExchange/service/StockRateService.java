package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.entity.StockRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockRateService {

    @Autowired
    private StockRateRepository stockRateRepository;

    public List<StockRate> findAllStockRates() {
        return stockRateRepository.findByActual(true);
    }
}
