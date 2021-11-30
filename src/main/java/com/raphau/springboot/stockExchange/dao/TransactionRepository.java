package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {



}
