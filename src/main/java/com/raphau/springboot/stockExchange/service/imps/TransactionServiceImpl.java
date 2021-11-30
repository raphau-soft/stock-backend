package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.TransactionRepository;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.Transaction;
import com.raphau.springboot.stockExchange.service.ints.TransactionService;
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
    public Map<String, Object> findAllTransactions() {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        List<Transaction> transactions =  transactionRepository.findAll();
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        Map<String, Object> objects = new HashMap<>();
        objects.put("transaction", transactions);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        for (Transaction transaction: transactions
             ) {
            log.info(transaction.toString());
        }
        return objects;
    }
}
