package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.service.ints.StockRateService;
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
    public Map<String, Object> findAllStockRates() {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        List<StockRate> stockRateList = stockRateRepository.findByActual(true);
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        Map<String, Object> objects = new HashMap<>();
        objects.put("stockRate", stockRateList);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return objects;
    }
}
