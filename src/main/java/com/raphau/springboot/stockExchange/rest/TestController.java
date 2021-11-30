package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.CpuDataRepository;
import com.raphau.springboot.stockExchange.entity.CpuData;
import com.raphau.springboot.stockExchange.entity.Test;
import com.raphau.springboot.stockExchange.service.TradingThread;
import com.raphau.springboot.stockExchange.service.ints.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Method;

import javax.sql.DataSource;
import java.lang.management.*;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins="*", maxAge = 3600)
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private TradingThread tradingThread;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CpuDataRepository cpuDataRepository;

    @GetMapping("/getTest")
    public ResponseEntity<?> getTest(){
        List<Test> tests = testService.getTest();
        List<CpuData> cpuData = cpuDataRepository.findAll();
        Map<String, Object> temp = new HashMap<>();
        temp.put("tests", tests);
        temp.put("cpuData", cpuData);
        return ResponseEntity.ok(temp);
    }

    @PostMapping("/setName")
    public void setName(@RequestBody String name){
        tradingThread.name = name;
    }

    @PostMapping("/cleanDB")
    public void cleanDB(){
        testService.cleanTestDB();
    }

    @PostMapping("/restartDB")
    public void restartDB(){
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("restart.sql"));
        resourceDatabasePopulator.execute(dataSource);
    }

    @GetMapping("/statistics")
    public void getStats(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.println(String.format("Initial memory: %.2f GB",
                (double)memoryMXBean.getHeapMemoryUsage().getInit() /1073741824));
        System.out.println(String.format("Used heap memory: %.2f GB",
                (double)memoryMXBean.getHeapMemoryUsage().getUsed() /1073741824));
        System.out.println(String.format("Max heap memory: %.2f GB",
                (double)memoryMXBean.getHeapMemoryUsage().getMax() /1073741824));
        System.out.println(String.format("Committed memory: %.2f GB",
                (double)memoryMXBean.getHeapMemoryUsage().getCommitted() /1073741824));


            OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
            for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
                method.setAccessible(true);
                if (method.getName().startsWith("get")
                        && Modifier.isPublic(method.getModifiers())) {
                    Object value;
                    try {
                        value = method.invoke(operatingSystemMXBean);
                    } catch (Exception e) {
                        value = e;
                    }
                    System.out.println(method.getName() + " = " + value);
                }
            }

    }

}


























