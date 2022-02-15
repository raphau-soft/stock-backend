package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.*;
import com.raphau.springboot.stockExchange.entity.Test;
import com.raphau.springboot.stockExchange.entity.TimeData;
import com.raphau.springboot.stockExchange.service.ints.TestService;
import com.raphau.springboot.stockExchange.service.ints.TradeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private CpuDataRepository cpuDataRepository;
    
    @Autowired
    private TimeDataRepository timeDataRepository;

    @Override
    public List<Test> getTest() {
        return testRepository.findAll();
    }
   

    @Override
    public void cleanTestDB(){
        testRepository.deleteAll();
        cpuDataRepository.deleteAll();
        timeDataRepository.deleteAll();
    }

    @Override
    public void restartDB(){

    }


	@Override
	public void startTest(String name) {
		Test test = new Test(0, name, System.currentTimeMillis(), false);
		testRepository.save(test);
		TradeServiceImpl.testStarted = true;
		TradeServiceImpl.test = test;
	}


	@Override
	public void finishTest() {
		Optional<Test> testOptional = testRepository.findByFinished(false);
		if(testOptional.isPresent()) {
			Test test = testOptional.get();
			test.setFinished(true);
			testRepository.save(test);
			TradeServiceImpl.testStarted = false;
		}
	}
}
