package com.raphau.springboot.stockExchange.service.implementation;

import com.raphau.springboot.stockExchange.dao.TransactionRepository;
import com.raphau.springboot.stockExchange.entity.Transaction;
import com.raphau.springboot.stockExchange.service.api.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    private static Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
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
