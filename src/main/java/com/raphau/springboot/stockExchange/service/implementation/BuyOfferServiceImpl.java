package com.raphau.springboot.stockExchange.service.implementation;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.api.BuyOfferService;
import com.raphau.springboot.stockExchange.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BuyOfferServiceImpl implements BuyOfferService {

    @Autowired
    private UserService userService;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Map<String, Object> getUserBuyOffers() {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        User user = userOpt.get();

        user.setPassword(null);
        List<com.raphau.springboot.stockExchange.entity.BuyOffer> buyOffers = user.getBuyOffers();
        buyOffers.removeIf(buyOffer -> !buyOffer.isActual());
        Map<String, Object> objects = new HashMap<>();
        objects.put("buyOffers", buyOffers);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return objects;
    }

    @Override
    public TestDetailsDTO deleteBuyOffer(int theId) {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        Optional<BuyOffer> buyOfferOptional = buyOfferRepository.findById(theId);
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }
        User user = userOpt.get();

        if(buyOfferOptional.isPresent()) {
            BuyOffer buyOffer = buyOfferOptional.get();
            if(user.getId() == buyOffer.getUser().getId()) {
                user.setMoney(user.getMoney().add(buyOffer.getMaxPrice().multiply(BigDecimal.valueOf(buyOffer.getAmount()))));
                buyOffer.setActual(false);
                timeBase = System.currentTimeMillis();
                buyOfferRepository.save(buyOffer);
                testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase + testDetailsDTO.getDatabaseTime());
            }
        }
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }

    @Override
    public TestDetailsDTO addOffer(BuyOfferDTO buyOfferDTO) {
        long timeApp = System.currentTimeMillis();
        long databaseTime = 0;
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        buyOfferDTO.setDateLimit(new Date());
        buyOfferDTO.setId(0);
        long timeDb = System.currentTimeMillis();
        User user = userRepository
                .findByUsername(buyOfferDTO.getUsername()).get();
        Company company = companyRepository
                .findById(buyOfferDTO.getCompany_id()).get();
        databaseTime += System.currentTimeMillis() - timeDb;
        if(user.getMoney().compareTo(buyOfferDTO.getMaxPrice()
                .multiply(BigDecimal.valueOf(buyOfferDTO.getAmount()))) < 0
                || buyOfferDTO.getAmount() <= 0){
            user.setMoney(BigDecimal.valueOf(100000));
        }
        buyOfferDTO.setId(0);
        BuyOffer buyOffer = new BuyOffer(buyOfferDTO, user, company);
        timeDb = System.currentTimeMillis();
        userRepository.save(user);
        buyOfferRepository.save(buyOffer);
        databaseTime += System.currentTimeMillis() - timeDb;
        testDetailsDTO.setDatabaseTime(databaseTime);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        testDetailsDTO.setTimestamp(timeApp);
        testDetailsDTO.setStockId(TradeServiceImpl.guid);
        testDetailsDTO.setId(buyOfferDTO.getTimeDataId());
        return testDetailsDTO;
    }
}
