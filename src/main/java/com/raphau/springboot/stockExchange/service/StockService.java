package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.StockRateNotFoundException;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private UserService userService;

    @Autowired
    private StockRateRepository stockRateRepository;

    public Map<String, Object> findResources() {
        String username = AuthUtils.getAuthenticatedUsername();
        User user = getUserByUsername(username);

        List<Stock> stocks = filterStocksWithPositiveAmount(user.getStocks());

        List<StockRate> stockRates = getStockRatesForStocks(stocks);

        Map<String, Object> result = new HashMap<>();
        result.put("stock", stocks);
        result.put("stockRate", stockRates);

        return result;
    }

    private User getUserByUsername(String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));
    }

    private List<Stock> filterStocksWithPositiveAmount(List<Stock> stocks) {
        return stocks.stream()
                .filter(stock -> stock.getAmount() > 0)
                .collect(Collectors.toList());
    }

    private List<StockRate> getStockRatesForStocks(List<Stock> stocks) {
        List<StockRate> stockRates = new ArrayList<>();
        for (Stock stock : stocks) {
            StockRate stockRate = stockRateRepository.findByCompanyAndActual(stock.getCompany(), true)
                    .orElseThrow(() -> new StockRateNotFoundException("Actual stock rate for " + stock.getCompany().getName() + " not found"));
            stockRates.add(stockRate);
        }
        return stockRates;
    }
}
