package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByUser(User user);
    List<Stock> findByCompany_Id(int company_Id);
    Optional<Stock> findByCompanyAndUser(Company company, User user);

}
