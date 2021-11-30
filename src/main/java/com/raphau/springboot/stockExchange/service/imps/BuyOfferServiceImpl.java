package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.CompanyNotFoundException;
import com.raphau.springboot.stockExchange.exception.NotEnoughMoneyException;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.TradeServiceImpl;
import com.raphau.springboot.stockExchange.service.ints.BuyOfferService;
import com.raphau.springboot.stockExchange.service.ints.UserService;
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

    // TODO: to change
    @Autowired
    private TradeServiceImpl tradeService;

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
    public TestDetailsDTO addOffer(BuyOfferDTO buyOfferDTO)
            throws InterruptedException {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        tradeService.trade(buyOfferDTO.getCompany_id(), buyOfferDTO, true);
        testDetailsDTO.setDatabaseTime(0);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }
}
