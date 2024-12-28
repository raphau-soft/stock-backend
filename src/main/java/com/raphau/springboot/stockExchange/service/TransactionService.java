package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.TransactionRepository;
import com.raphau.springboot.stockExchange.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    private static Logger log = LoggerFactory.getLogger(TransactionService.class);

    @Deprecated
    public List<Transaction> findAllTransactions() {
//        List<Transaction> transactions =  transactionRepository.findAll();
//        for (Transaction transaction: transactions) {
//            log.info(transaction.toString());
//        }
//        return transactions;
        return null;
    }
}
