package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.service.imps.TradeServiceImpl;
import com.raphau.springboot.stockExchange.service.ints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @RabbitListener(queues = "buy-offer-request")
    public void receiveBuyOfferMessage(BuyOfferDTO buyOfferDTO) throws InterruptedException {
        logger.info("Buy offer received: " + buyOfferDTO.toString());
        TestDetailsDTO testDetailsDTO = buyOfferService.addOffer(buyOfferDTO);
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
    }

    @RabbitListener(queues = "sell-offer-request")
    public void receiveSellOfferMessage(SellOfferDTO sellOfferDTO) throws InterruptedException {
        logger.info("Sell offer received: " + sellOfferDTO.toString());
        TestDetailsDTO testDetailsDTO = sellOfferService.addSellOffer(sellOfferDTO);
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
    }

    @RabbitListener(queues = "register-request")
    public void receiveRegisterMessage(String username) {
        logger.info("User register: " + username);
        User user = new User(0, "John", "White",
                username, encoder.encode("password"), new BigDecimal(1000000), "abc@gma.com",
                "ROLE_USER");
        userRepository.save(user);
        rabbitTemplate.convertAndSend("register-response-exchange", "foo.bar.#", "1");
    }

    @RabbitListener(queues = "company-request")
    public void receiveCompanyMessage(CompanyDTO companyDTO) {
        logger.info("Company received: " + companyDTO.toString());
        TestDetailsDTO testDetailsDTO = companyService.addCompany(companyDTO);
        rabbitTemplate.convertAndSend("test-details-exchange", "foo.bar.#", testDetailsDTO);
    }

    @RabbitListener(queues = "stock-data-request")
    public void receiveStockRequest(String username) {
        logger.info("Stock request received: " + username);
        Map<String, Object> objects =  stockService.findResources(username);
        rabbitTemplate.convertAndSend("stock-data-response-exchange", "foo.bar.#", objects);
    }

    @RabbitListener(queues = "user-data-request")
    public void receiveUserRequest(String username) {
        logger.info("User received: " + username);
        Map<String, Object> objects = new HashMap<>();
        Optional<User> user = userService.findByUsername(username);
        List<Company> companyList = companyRepository.findAll();
        List<StockRate> stockRates = stockRateRepository.findByActual(true);
        objects.put("username", username);
        objects.put("user", user.get());
        objects.put("companies", companyList);
        objects.put("stockRates", stockRates);
        rabbitTemplate.convertAndSend("user-data-response-exchange", "foo.bar.#", objects);
    }

    @RabbitListener(queues = "trade-request")
    public void receiveTradeRequest(String tick) throws InterruptedException {
        logger.info("Received trade tick: " + tick);
        switch(tick) {
            case "0":
                tradeService.trade();
                break;
            case "1":
                tradeService.clearDB();
                break;
        }
    }
}
