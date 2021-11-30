package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.ints.CompanyService;
import com.raphau.springboot.stockExchange.service.ints.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Map<String, Object> findAllCompanies() {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        List<Company> companies = companyRepository.findAll();
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        Map<String, Object> objects = new HashMap<>();
        objects.put("company", companies);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return objects;
    }

    @Override
    public TestDetailsDTO addCompany(CompanyDTO companyDTO) {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        if (!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }
        User user = userOpt.get();

        companyDTO.setId(0);
        Company company = new Company(companyDTO.getId(), companyDTO.getName());
        Stock stock = new Stock(0, user, company, companyDTO.getAmount());
        StockRate stockRate = new StockRate(0, company, companyDTO.getPrice(), new Date(), true);

        timeBase = System.currentTimeMillis();
        companyRepository.save(company);
        stockRepository.save(stock);
        stockRateRepository.save(stockRate);
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase + testDetailsDTO.getDatabaseTime());

        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }
}
