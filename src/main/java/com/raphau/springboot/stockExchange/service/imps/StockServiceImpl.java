package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.StockRateNotFoundException;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.ints.StockService;
import com.raphau.springboot.stockExchange.service.ints.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private UserService userService;

    @Autowired
    private StockRateRepository stockRateRepository;

    @Override
    public Map<String, Object> findResources() {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }
        User user = userOpt.get();
        List<Stock> stocks = user.getStocks();
        List<StockRate> stockRates = new ArrayList<>();
        stocks.removeIf(stock -> stock.getAmount() == 0);
        for (Stock stock : stocks) {
            timeBase = System.currentTimeMillis();
            Optional<StockRate> stockRate = stockRateRepository.findByCompanyAndActual(stock.getCompany(), true);
            testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase + testDetailsDTO.getDatabaseTime());
            if(!stockRate.isPresent()){
                throw new StockRateNotFoundException("Actual stock rate for " + stock.getCompany().getName() + " not found");
            }
            stockRates.add(stockRate.get());
        }

        Map<String, Object> objects = new HashMap<>();
        objects.put("stock", stocks);
        objects.put("stockRate", stockRates);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return objects;
    }
}
