package com.raphau.springboot.stockExchange.service.implementation;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.service.api.CompanyService;
import com.raphau.springboot.stockExchange.service.api.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockRateRepository stockRateRepository;

    @Override
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    @Transactional
    public void addCompany(CompanyDTO companyDTO) {
        User user = userService.findByUsername(companyDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User with username '" + companyDTO.getUsername() + "' not found"));

        Company company = new Company(companyDTO.getName());
        Stock stock = new Stock(user, company, companyDTO.getAmount());
        StockRate stockRate = new StockRate(company, companyDTO.getPrice(), new Date(), true);

        companyRepository.save(company);
        stockRepository.save(stock);
        stockRateRepository.save(stockRate);
    }
}
