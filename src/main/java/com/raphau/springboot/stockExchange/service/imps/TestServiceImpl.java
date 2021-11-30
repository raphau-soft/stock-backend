package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.*;
import com.raphau.springboot.stockExchange.entity.Test;
import com.raphau.springboot.stockExchange.service.ints.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private CpuDataRepository cpuDataRepository;

    @Override
    public List<Test> getTest() {
        return testRepository.findAll();
    }

    @Override
    public void cleanTestDB(){
        testRepository.deleteAll();
        cpuDataRepository.deleteAll();
    }

    @Override
    public void restartDB(){

    }
}
