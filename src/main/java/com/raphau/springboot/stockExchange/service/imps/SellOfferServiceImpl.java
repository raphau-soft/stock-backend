package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.CompanyNotFoundException;
import com.raphau.springboot.stockExchange.exception.StockAmountException;
import com.raphau.springboot.stockExchange.exception.StockNotFoundException;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.ints.SellOfferService;
import com.raphau.springboot.stockExchange.service.ints.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class SellOfferServiceImpl implements SellOfferService {

    @Autowired
    private UserService userService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Override
    public Map<String, Object> getUserSellOffers() {
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

        List<SellOffer> sellOffers = user.getSellOffers();
        sellOffers.removeIf(sellOffer -> !sellOffer.isActual());

        Map<String, Object> objects = new HashMap<>();
        objects.put("sellOffers", sellOffers);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return objects;
    }

    @Override
    public TestDetailsDTO deleteSellOffer(int theId) {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        Optional<SellOffer> sellOfferOptional = sellOfferRepository.findById(theId);
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        User user = userOpt.get();

        if(sellOfferOptional.isPresent()) {
            SellOffer sellOffer = sellOfferOptional.get();
            Stock stock = sellOffer.getStock();
            if(stock.getUser().getId() == user.getId()) {
                stock.setAmount(stock.getAmount() + sellOffer.getAmount());
                sellOffer.setActual(false);
                timeBase = System.currentTimeMillis();
                stockRepository.save(stock);
                sellOfferRepository.save(sellOffer);
                testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase + testDetailsDTO.getDatabaseTime());
            }
        }
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }

    @Override
    @Transactional
    public TestDetailsDTO addSellOffer(SellOfferDTO sellOfferDTO) {
        long timeApp = System.currentTimeMillis();
        long databaseTime = 0;
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Calendar c = Calendar.getInstance();
        c.setTime(sellOfferDTO.getDateLimit());
        c.add(Calendar.DATE, 1);
        sellOfferDTO.setDateLimit(c.getTime());
        sellOfferDTO.setId(0);
        long timeDb = System.currentTimeMillis();
        Company company = companyRepository.getOne(sellOfferDTO.getCompany_id());
        User user = userService.findByUsername(sellOfferDTO.getUsername()).get();
        Stock stock = stockRepository.findByCompanyAndUser(company, user).get();
        databaseTime += System.currentTimeMillis() - timeDb;
        if(sellOfferDTO.getAmount() > stock.getAmount() || sellOfferDTO.getAmount() <= 0){
            throw new StockAmountException("Wrong amount of resources - stock " + stock.getAmount() + " - sellOffer " + sellOfferDTO.getAmount());
        }
        stock.setAmount(stock.getAmount() - sellOfferDTO.getAmount());
        SellOffer sellOffer = new SellOffer(sellOfferDTO, user, stock);
        timeDb = System.currentTimeMillis();
        stockRepository.save(stock);
        sellOfferRepository.save(sellOffer);
        databaseTime += System.currentTimeMillis() - timeDb;
        testDetailsDTO.setDatabaseTime(databaseTime);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        testDetailsDTO.setTimestamp(timeApp);
        testDetailsDTO.setEndpointUrl("add-sell-offer");
        testDetailsDTO.setMethod("POST");
        return testDetailsDTO;
    }


}



























