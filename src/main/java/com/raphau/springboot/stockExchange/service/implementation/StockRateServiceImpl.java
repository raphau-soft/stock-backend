package com.raphau.springboot.stockExchange.service.implementation;

import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.service.api.StockRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockRateServiceImpl implements StockRateService {

    @Autowired
    private StockRateRepository stockRateRepository;

    @Override
    public List<StockRate> findAllStockRates() {
        return stockRateRepository.findByActual(true);
    }
}
