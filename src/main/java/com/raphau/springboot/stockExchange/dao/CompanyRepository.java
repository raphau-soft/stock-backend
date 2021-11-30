package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {



}
