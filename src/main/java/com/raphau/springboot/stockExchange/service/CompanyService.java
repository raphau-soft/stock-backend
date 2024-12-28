package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockRateRepository stockRateRepository;

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional
    public void addCompany(CompanyDTO companyDTO) {
        String username = AuthUtils.getAuthenticatedUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        Company company = new Company(companyDTO.getName());
        Stock stock = new Stock(user, company, companyDTO.getAmount());
        StockRate stockRate = new StockRate(company, companyDTO.getPrice(), new Date(), true);

        companyRepository.save(company);
        stockRepository.save(stock);
        stockRateRepository.save(stockRate);
    }
}
