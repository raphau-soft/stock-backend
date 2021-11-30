package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.*;
import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.*;
import com.raphau.springboot.stockExchange.exception.*;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Semaphore;

@Service
public class TradingThread {

    private final int OFFERS_NUMBER = 5;
    public String name;
    private final OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    @Autowired
    private BuyOfferRepository buyOfferRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private SellOfferRepository sellOfferRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private StockRateRepository stockRateRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CpuDataRepository cpuDataRepository;

    Logger log = LoggerFactory.getLogger(TradingThread.class);


    @Async("asyncExecutor")
    public void run(int companyId, Semaphore semaphore, Object dto, boolean flag) {
        Test test = new Test();
        test.setName(name);
        try {
            long semaphoreTime = System.currentTimeMillis();
            semaphore.acquire();
            test.setSemaphoreWaitTime(System.currentTimeMillis() - semaphoreTime);
            if(flag){
                addBuyOffer((BuyOfferDTO) dto);
            } else {
                addSellOffer((SellOfferDTO) dto);
            }

            List<SellOffer>  sellOffers = new ArrayList<>();
            long timeDB = System.currentTimeMillis();
            List<BuyOffer> buyOffers = new ArrayList<>(buyOfferRepository
                    .findByCompany_IdAndActual(companyId, true));
            List<Stock> stocks = stockRepository.findByCompany_Id(companyId);
            test.setDatabaseTime(System.currentTimeMillis() - timeDB);
            stocks.forEach(stock -> sellOffers.addAll(stock.getSellOffers()));
            sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
            buyOffers.sort(new SortBuyOffers());
            sellOffers.sort(new SortSellOffers());


            List<Transaction> transactions = new ArrayList<>();
            // trade with three variants (more stocks on sell/buy offer, or even)
            test.setApplicationTime(0);
            int i = 0;
            while(buyOffers.size() >= OFFERS_NUMBER
                    && sellOffers.size() >= OFFERS_NUMBER){
                i++;
                if(!startTrading(buyOffers.subList(0, OFFERS_NUMBER),
                        sellOffers.subList(0, OFFERS_NUMBER), transactions, test)){
                    break;
                }
                sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
                buyOffers.removeIf(buyOffer -> !buyOffer.isActual());
            }
            // update stock rate
            if(!transactions.isEmpty()) {
                log.info("Saving test " + test.toString());
                test.setApplicationTime(test.getApplicationTime()/i);
                testRepository.save(test);
                updateStockRates(companyId, transactions, test);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private synchronized void addBuyOffer(BuyOfferDTO buyOfferDTO){
        Calendar c = Calendar.getInstance();
        c.setTime(buyOfferDTO.getDateLimit());
        c.add(Calendar.DATE, 1);
        buyOfferDTO.setDateLimit(c.getTime());
        buyOfferDTO.setId(0);
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOptional = userRepository
                .findByUsername(userDetails.getUsername());
        Optional<Company> companyOptional = companyRepository
                .findById(buyOfferDTO.getCompany_id());
        if(!companyOptional.isPresent()){
            throw new CompanyNotFoundException("Company with id "
                    + buyOfferDTO.getCompany_id() + " not found");
        }
        if(!userOptional.isPresent()){
            throw new UserNotFoundException("User" +
                    userDetails.getUsername() + " not found");
        }
        User user = userOptional.get();
        Company company = companyOptional.get();
        if(user.getMoney().compareTo(buyOfferDTO.getMaxPrice()
                .multiply(BigDecimal.valueOf(buyOfferDTO.getAmount()))) < 0
                || buyOfferDTO.getAmount() <= 0){
//            throw new NotEnoughMoneyException("Not enough money (Amount is "
//                    + buyOfferDTO.getAmount() + "). You have "
//                    + user.getMoney().toString() + " " +
//                    "but you need " + buyOfferDTO.getMaxPrice().
//                    multiply(BigDecimal.valueOf(buyOfferDTO.getAmount()))
//                    .toString());
            return;
        }
        BuyOffer buyOffer = new BuyOffer(0, company, user,
                buyOfferDTO.getMaxPrice(), buyOfferDTO.getAmount(),
                buyOfferDTO.getAmount(), buyOfferDTO.getDateLimit(),
                true);
        user.setMoney(user.getMoney().subtract(buyOfferDTO.getMaxPrice()
                .multiply(BigDecimal.valueOf(buyOfferDTO.getAmount()))));
        userRepository.save(user);
        buyOfferRepository.save(buyOffer);
    }

    private synchronized void addSellOffer(SellOfferDTO sellOfferDTO){
        Calendar c = Calendar.getInstance();
        c.setTime(sellOfferDTO.getDateLimit());
        c.add(Calendar.DATE, 1);
        sellOfferDTO.setDateLimit(c.getTime());
        sellOfferDTO.setId(0);
        Optional<User> userOpt = getUser();
        Optional<Company> companyOptional = companyRepository.findById(sellOfferDTO.getCompany_id());
        if(!companyOptional.isPresent()){
            throw new CompanyNotFoundException("Company with id " + sellOfferDTO.getCompany_id() + " not found");
        }
        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User not found");
        }
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(companyOptional.get(), userOpt.get());
        if(!stockOptional.isPresent()){
            return;
        }
        Stock stock = stockOptional.get();
        if(sellOfferDTO.getAmount() > stock.getAmount() || sellOfferDTO.getAmount() <= 0){
            throw new StockAmountException("Wrong amount of resources - stock " + stock.getAmount() + " - sellOffer " + sellOfferDTO.getAmount());
        }
        stock.setAmount(stock.getAmount() - sellOfferDTO.getAmount());
        SellOffer sellOffer = new SellOffer(sellOfferDTO, stock);
        //log.info("Saving stock in addSellOffer " + stock.toString());
        stockRepository.save(stock);
        sellOfferRepository.save(sellOffer);
        //log.info("Saving sellOffer in addSellOffer " + sellOffer.toString());
    }

    private Optional<User> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }


    private synchronized void updateStockRates(int companyId, List<Transaction> transactions, Test test){
        double price = 0;
        int amount = 0;
        for(Transaction transaction: transactions){
            price += transaction.getPrice() * transaction.getAmount();
            amount += transaction.getAmount();
        }

        double average = price / amount;
        long timeDB = System.currentTimeMillis();
        Optional<StockRate> stockRateOptional = stockRateRepository.findByCompany_IdAndActual(companyId, true);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);

        if(stockRateOptional.isPresent()){
            StockRate stockRate = stockRateOptional.get();
            double dif = average - stockRate.getRate();
            dif = dif/10;
            double newPrice = stockRate.getRate() + dif;
            newPrice = Math.round(newPrice*100) / 100.0;
            StockRate newStockRate = new StockRate(0, stockRate.getCompany(), newPrice, new Date(), true);
            stockRate.setActual(false);
            timeDB = System.currentTimeMillis();
            //log.info("Saving stockRate in updateStockRates " + stockRate.toString());
            stockRateRepository.save(stockRate);
            //log.info("Saving newStockRate in updateStockRate " + newStockRate.toString());
            stockRateRepository.save(newStockRate);
            test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        }

    }

    private boolean startTrading(List<BuyOffer> buyOffers,
                                 List<SellOffer> sellOffers,
                                 List<Transaction> transactions,
                                 Test test) {
        long appTime = System.currentTimeMillis();
        BuyOffer buyOffer;
        SellOffer sellOffer;
        int i=0,j=0;
        while(i < buyOffers.size() && j < sellOffers.size()){
            buyOffer = buyOffers.get(i);
            sellOffer = sellOffers.get(j);
            if(buyOffer.getMaxPrice().compareTo(sellOffer.getMinPrice()) < 0)
                return false;
            if(buyOffer.getAmount() > sellOffer.getAmount()){
                transactions.add(buyOfferStay(buyOffer, sellOffer, test));
                j++;
            } else if (buyOffer.getAmount() < sellOffer.getAmount()){
                transactions.add(sellOfferStay(buyOffer, sellOffer, test));
                i++;
            } else {
                transactions.add(noneOfferStay(buyOffer,sellOffer, test));
                i++;j++;
            }
        }
        for (Method method : bean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("getSystem")
                    && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(bean);
                } catch (Exception e) {
                    value = e;
                }
                CpuData cpuData = new CpuData(0, name,
                        System.currentTimeMillis(), (Double) value);
                cpuDataRepository.save(cpuData);
            }
        }
        test.setApplicationTime(System.currentTimeMillis() - appTime);
        return true;
    }

    //        for(int i = 0; i < buyOffers.size();){
//            buyOffer = buyOffers.get(i);
//            for(int j = 0; j < sellOffers.size();){
//                sellOffer = sellOffers.get(j);
//                if(!sellOffer.isActual()) continue;
//                if(buyOffer.getMaxPrice().compareTo(sellOffer.getMinPrice()) < 0) return false;
//                if(buyOffer.getAmount() > sellOffer.getAmount()){
//                    buyOfferStay(buyOffer, sellOffer);
//                    j++;
//                } else if (buyOffer.getAmount() < sellOffer.getAmount()){
//                    sellOfferStay(buyOffer, sellOffer);
//                    i++;
//                } else {
//                    noneOfferStay(buyOffer,sellOffer);
//                    i++;j++;
//                }
//            }
//        }

    private synchronized Transaction noneOfferStay
            (BuyOffer buyOffer, SellOffer sellOffer, Test test){
        double price = (buyOffer.getMaxPrice().doubleValue()
                + sellOffer.getMinPrice().doubleValue())/2;
        Transaction transaction = new Transaction(0, buyOffer,
                sellOffer, sellOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getStock().getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().
                add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price))
                        .multiply(new BigDecimal(sellOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney()
                .add(new BigDecimal(price * sellOffer.getAmount())));
        long timeDB = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository
                .findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        test.setDatabaseTime(test.getDatabaseTime()
                + System.currentTimeMillis() - timeDB);
        Stock stock;
        if(!stockOptional.isPresent()) {
            stock = new Stock(0, buyOfferOwner,
                    buyOffer.getCompany(), sellOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + sellOffer.getAmount());
        }
        buyOffer.setAmount(0);
        buyOffer.setActual(false);
        sellOffer.setAmount(0);
        sellOffer.setActual(false);
        timeDB = System.currentTimeMillis();
        stockRepository.save(stock);
        transactionRepository.save(transaction);
        buyOfferRepository.save(buyOffer);
        sellOfferRepository.save(sellOffer);
        test.setDatabaseTime(test.getDatabaseTime()
                + System.currentTimeMillis() - timeDB);
        return transaction;
    }

    private synchronized Transaction buyOfferStay(BuyOffer buyOffer, SellOffer sellOffer, Test test){
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, sellOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getStock().getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(sellOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * sellOffer.getAmount())));
        long timeDB = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        Stock stock;
        if(!stockOptional.isPresent()) {
            stock = new Stock(0, buyOfferOwner, buyOffer.getCompany(), sellOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + sellOffer.getAmount());
        }
        buyOffer.setAmount(buyOffer.getAmount() - sellOffer.getAmount());
        sellOffer.setAmount(0);
        sellOffer.setActual(false);
        timeDB = System.currentTimeMillis();
        //log.info("Saving stock in buyOfferStay " + stock.toString());
        stockRepository.save(stock);
        //log.info("Saving transaction in buyOfferStay " + transaction.toString());
        transactionRepository.save(transaction);
        //log.info("Saving buyOffer in buyOfferStay " + buyOffer.toString());
        buyOfferRepository.save(buyOffer);
        //log.info("Saving sellOffer in buyOfferStay " + sellOffer.toString());
        sellOfferRepository.save(sellOffer);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        return transaction;
    }
    private synchronized Transaction sellOfferStay(BuyOffer buyOffer, SellOffer sellOffer, Test test){
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, buyOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getStock().getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(buyOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * buyOffer.getAmount())));
        long timeDB = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        Stock stock;
        if(!stockOptional.isPresent()) {
            stock = new Stock(0, buyOfferOwner, buyOffer.getCompany(), buyOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + buyOffer.getAmount());
        }
        sellOffer.setAmount(sellOffer.getAmount() - buyOffer.getAmount());
        buyOffer.setAmount(0);
        buyOffer.setActual(false);
        timeDB = System.currentTimeMillis();
        //log.info("Saving stock in sellOfferStay " + stock.toString());
        stockRepository.save(stock);
        //log.info("Saving transaction in sellOfferStay " + transaction.toString());
        transactionRepository.save(transaction);
        //log.info("Saving sellOffer in sellOfferStay " + sellOffer.toString());
        sellOfferRepository.save(sellOffer);
        //log.info("Saving buyOffer in sellOfferStay " + buyOffer.toString());
        buyOfferRepository.save(buyOffer);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        return transaction;
    }

    static class SortBuyOffers implements Comparator<BuyOffer>{
        public int compare(BuyOffer a, BuyOffer b){
            return b.getMaxPrice().compareTo(a.getMaxPrice());
        }
    }

    static class SortSellOffers implements Comparator<SellOffer>{
        public int compare(SellOffer a, SellOffer b){
            return a.getMinPrice().compareTo(b.getMinPrice());
        }
    }

}
