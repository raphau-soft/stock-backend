package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.StockRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRateRepository extends JpaRepository<StockRate, Integer> {
    Optional<StockRate> findByCompanyAndActual(Company company, Boolean actual);
    Optional<StockRate> findByCompany_IdAndActual(int company_id, Boolean actual);
    List<StockRate> findByActual(Boolean actual);
}
