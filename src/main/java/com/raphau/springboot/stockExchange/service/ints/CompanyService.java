package com.raphau.springboot.stockExchange.service.ints;

import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;

import java.util.Map;

public interface CompanyService {

    Map<String, Object> findAllCompanies();
    TestDetailsDTO addCompany(CompanyDTO companyDTO);

}
