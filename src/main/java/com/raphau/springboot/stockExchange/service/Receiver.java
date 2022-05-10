package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.*;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.service.implementation.TradeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Receiver {

    Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    private BuyOfferService buyOfferService;

    @Autowired
    private SellOfferService sellOfferService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRateRepository stockRateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin admin;

    @RabbitListener(queues = "buy-offer-request")
    public void receiveBuyOfferMessage(BuyOfferDTO buyOfferDTO) throws InterruptedException {
        logger.debug("Buy offer received: " + buyOfferDTO.toString());
        TestDetailsDTO testDetailsDTO = buyOfferService.addOffer(buyOfferDTO);
        QueueInformation information = admin.getQueueInfo("test-details-response");
        assert information != null;
        testDetailsDTO.setQueueSizeBack(information.getMessageCount());
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
    }

    @RabbitListener(queues = "sell-offer-request")
    public void receiveSellOfferMessage(SellOfferDTO sellOfferDTO) throws InterruptedException {
        logger.debug("Sell offer received: " + sellOfferDTO.toString());
        TestDetailsDTO testDetailsDTO = sellOfferService.addSellOffer(sellOfferDTO);
        QueueInformation information = admin.getQueueInfo("test-details-response");
        assert information != null;
        testDetailsDTO.setQueueSizeBack(information.getMessageCount());
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
    }

    @RabbitListener(queues = "register-request")
    public void receiveRegisterMessage(GetDataDTO dataDTO) {
        long timeApp = System.currentTimeMillis();
        long appTime = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        User user = new User(0, "John", "White",
                dataDTO.getUsername(), encoder.encode("password"), new BigDecimal(100000), "abc@gma.com",
                "ROLE_USER");
        long dbTime = System.currentTimeMillis();
        userRepository.save(user);
        dbTime = System.currentTimeMillis() - dbTime;
        testDetailsDTO.setStockId(TradeServiceImpl.guid);
        testDetailsDTO.setTimestamp(timeApp);
        testDetailsDTO.setDatabaseTime(dbTime);
        testDetailsDTO.setId(dataDTO.getTimeDataId());
        QueueInformation information = admin.getQueueInfo("test-details-response");
        assert information != null;
        testDetailsDTO.setQueueSizeBack(information.getMessageCount());
        appTime = System.currentTimeMillis() - appTime;
        testDetailsDTO.setApplicationTime(appTime);
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
        rabbitTemplate.convertAndSend("register-response-exchange", "foo.bar.#", "0");
    }

    @RabbitListener(queues = "company-request")
    public void receiveCompanyMessage(CompanyDTO companyDTO) {
        TestDetailsDTO testDetailsDTO = companyService.addCompany(companyDTO);
        QueueInformation information = admin.getQueueInfo("test-details-response");
        assert information != null;
        testDetailsDTO.setQueueSizeBack(information.getMessageCount());
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
    }

    @RabbitListener(queues = "stock-data-request")
    @Transactional
    public void receiveStockRequest(GetDataDTO dataDTO) {
        long timeApp = System.currentTimeMillis();
        long appTime = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long dbTime = System.currentTimeMillis();
        Map<String, Object> objects =  stockService.findResources(dataDTO.getUsername());
        dbTime = System.currentTimeMillis() - dbTime;
        testDetailsDTO.setStockId(TradeServiceImpl.guid);
        testDetailsDTO.setTimestamp(timeApp);
        testDetailsDTO.setDatabaseTime(dbTime);
        QueueInformation information = admin.getQueueInfo("test-details-response");
        assert information != null;
        testDetailsDTO.setQueueSizeBack(information.getMessageCount());
        appTime = System.currentTimeMillis() - appTime;
        testDetailsDTO.setId(dataDTO.getTimeDataId());
        testDetailsDTO.setApplicationTime(appTime);
        rabbitTemplate.convertAndSend("stock-data-response-exchange", "foo.bar.#", objects);
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
    }

    @RabbitListener(queues = "user-data-request")
    public void receiveUserRequest(GetDataDTO dataDTO) {
        long timeApp = System.currentTimeMillis();
        long appTime = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Map<String, Object> objects = new HashMap<>();
        long dbTime = System.currentTimeMillis();
        Optional<User> user = userService.findByUsername(dataDTO.getUsername());
        List<Company> companyList = companyRepository.findAll();
        List<StockRate> stockRates = stockRateRepository.findByActual(true);
        dbTime = System.currentTimeMillis() - dbTime;
        objects.put("username", dataDTO.getUsername());
        objects.put("user", user.get());
        objects.put("companies", companyList);
        objects.put("stockRates", stockRates);
        testDetailsDTO.setStockId(TradeServiceImpl.guid);
        testDetailsDTO.setTimestamp(timeApp);
        testDetailsDTO.setDatabaseTime(dbTime);
        QueueInformation information = admin.getQueueInfo("test-details-response");
        assert information != null;
        testDetailsDTO.setQueueSizeBack(information.getMessageCount());
        appTime = System.currentTimeMillis() - appTime;
        testDetailsDTO.setApplicationTime(appTime);
        testDetailsDTO.setId(dataDTO.getTimeDataId());
        rabbitTemplate.convertAndSend("user-data-response-exchange", "foo.bar.#", objects);
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
    }

    @RabbitListener(queues = "trade-request")
    public void receiveTradeRequest(String tick) throws Exception {
        logger.info("Received trade tick: " + tick);
        switch(tick) {
            case "0":
                logger.info("Trading started...");
                tradeService.finishTrading(false);
                tradeService.trade();
                break;
            case "1":
                logger.info("Clearing database...");
                tradeService.clearDB();
                break;
            case "2":
                if(!TradeServiceImpl.finishTrading) {
                    logger.info("Finish trading...");
                    tradeService.finishTrading(true);
                }
                break;
        }
    }
}
