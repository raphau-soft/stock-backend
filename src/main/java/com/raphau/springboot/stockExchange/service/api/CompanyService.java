package com.raphau.springboot.stockExchange.service.api;

import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.entity.Company;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    List<Company> findAllCompanies();
    void addCompany(CompanyDTO companyDTO);
}
