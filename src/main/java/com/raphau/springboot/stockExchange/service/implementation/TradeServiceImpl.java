package com.raphau.springboot.stockExchange.service.implementation;

import com.raphau.springboot.stockExchange.dao.*;
import com.raphau.springboot.stockExchange.dto.TimeDataDTO;
import com.raphau.springboot.stockExchange.entity.*;
import com.raphau.springboot.stockExchange.service.api.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.util.*;


@Service
public class TradeServiceImpl implements TradeService {

    Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);
    private long databaseTime;
    private final OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
    private long numberOfSellOffers;
    private long numberOfBuyOffers;
    private long buyOfferStayTime;
    private long sellOfferStayTime;
    private long noneStayTime;
    public static String guid = UUID.randomUUID().toString();
    public static boolean finishTrading = true;

    @Autowired
    private BuyOfferRepository buyOfferRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellOfferRepository sellOfferRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private StockRateRepository stockRateRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void addStocks() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            if (stock.getAmount() == 0) {
                stockRepository.delete(stock);
                continue;
            }
            stock.setAmount(10 + stock.getAmount());
            stockRepository.save(stock);
        }
    }

    @Override
    @Async("asyncExecutor")
    public void trade() {
        long applicationTime = System.currentTimeMillis();
        databaseTime = 0;
        numberOfBuyOffers = 0;
        numberOfSellOffers = 0;
        long getBuyOffersTime = 0;
        long getStocksTime = 0;
        long getSellOffersTime = 0;
        long getUpdateStocks = 0;
        long getTradingTime = 0;
        buyOfferStayTime = 0;
        sellOfferStayTime = 0;
        noneStayTime = 0;
        long getCompaniesTime = System.currentTimeMillis();
        List<Company> companies = companyRepository.findAll();
        getCompaniesTime = System.currentTimeMillis() - getCompaniesTime;
        for (Company company : companies) {
            logger.info("Trading for company: " + company.getName());
            long dbTime = System.currentTimeMillis();

            long temp = System.currentTimeMillis();
            List<BuyOffer> buyOffers = new ArrayList<>(buyOfferRepository
                    .findTop10ByCompany_IdAndActualOrderByMaxPriceDesc(company.getId(), true));
            getBuyOffersTime += System.currentTimeMillis() - temp;

            temp = System.currentTimeMillis();
            List<Stock> stocks = stockRepository.findByCompany_Id(company.getId());
            getStocksTime += System.currentTimeMillis() - temp;

            databaseTime += System.currentTimeMillis() - dbTime;
            List<SellOffer> sellOffers = new ArrayList<>();

            temp = System.currentTimeMillis();
            stocks.forEach(stock -> sellOffers.addAll(stock.getSellOffers()));
            sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
            getSellOffersTime += System.currentTimeMillis() - temp;

            buyOffers.sort(new SortBuyOffers());
            sellOffers.sort(new SortSellOffers());
            List<Transaction> transactions = new ArrayList<>();
            while (buyOffers.size() > 0
                    && sellOffers.size() > 0) {

                temp = System.currentTimeMillis();
                if (finishTrading || !startTrading(buyOffers.subList(0, buyOffers.size()),
                        sellOffers.subList(0, sellOffers.size()), transactions)) {
                    break;
                }
                getTradingTime += System.currentTimeMillis() - temp;

                sellOffers.removeIf(sellOffer -> !sellOffer.isActual());

                temp = System.currentTimeMillis();
                buyOffers = new ArrayList<>(buyOfferRepository
                        .findTop10ByCompany_IdAndActualOrderByMaxPriceDesc(company.getId(), true));
                getBuyOffersTime += System.currentTimeMillis() - temp;

            }
            if (!transactions.isEmpty()) {
                temp = System.currentTimeMillis();
                updateStockRates(company.getId(), transactions);
                getUpdateStocks += System.currentTimeMillis() - temp;
            }
        }
        applicationTime = System.currentTimeMillis() - applicationTime;
        TimeDataDTO timeDataDTO = new TimeDataDTO(System.currentTimeMillis(), databaseTime, applicationTime, guid);
        databaseTime = 0;
        timeDataDTO.setNumberOfBuyOffers(numberOfBuyOffers);
        timeDataDTO.setNumberOfSellOffers(numberOfSellOffers);
        logger.info("Sending trade response tick");
        this.finishTrading(true);
        logger.info("XYZ1 Get companies time: " + getCompaniesTime);
        logger.info("XYZ2 Get buy offers time: " + getBuyOffersTime);
        logger.info("XYZ3 Get trading time: " + getTradingTime);
        logger.info("XYZ4 Get stocks time: " + getStocksTime);
        logger.info("XYZ5 Get sell offers time: " + getSellOffersTime);
        logger.info("XYZ6 Get update stocks time: " + getUpdateStocks);
        logger.info("XYZ7 Get buyOfferStay time: " + buyOfferStayTime);
        logger.info("XYZ8 Get sellOfferStay time: " + sellOfferStayTime);
        logger.info("XYZ9 Get noneOfferStay time: " + noneStayTime);

    }

    public void finishTrading(boolean finish) {
        finishTrading = finish;
    }

    boolean startTrading(List<BuyOffer> buyOffers,
                         List<SellOffer> sellOffers, List<Transaction> transactions) {
        int i = 0, j = 0;
        long temp;
        while (i < buyOffers.size() && j < sellOffers.size()) {
            BuyOffer buyOffer = buyOffers.get(i);
            SellOffer sellOffer = sellOffers.get(j);
            if (buyOffer.getMaxPrice().compareTo(sellOffer.getMinPrice()) < 0)
                return false;
            if (buyOffer.getAmount() > sellOffer.getAmount()) {
                temp = System.currentTimeMillis();
                transactions.add(buyOfferStay(buyOffer, sellOffer));
                buyOfferStayTime += System.currentTimeMillis() - temp;
                j++;
            } else if (buyOffer.getAmount() < sellOffer.getAmount()) {
                temp = System.currentTimeMillis();
                transactions.add(sellOfferStay(buyOffer, sellOffer));
                sellOfferStayTime += System.currentTimeMillis() - temp;
                i++;
            } else {
                temp = System.currentTimeMillis();
                transactions.add(noneOfferStay(buyOffer, sellOffer));
                noneStayTime += System.currentTimeMillis() - temp;
                i++;
                j++;
            }
        }
        return true;
    }

    Transaction noneOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        double price = (buyOffer.getMaxPrice().doubleValue()
                + sellOffer.getMinPrice().doubleValue()) / 2;
        Transaction transaction = new Transaction(0, buyOffer,
                sellOffer, sellOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price))
                .multiply(new BigDecimal(sellOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney()
                .add(new BigDecimal(price * sellOffer.getAmount())));
        long dbTime = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository
                .findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        databaseTime += System.currentTimeMillis() - dbTime;
        Stock stock;
        if (!stockOptional.isPresent()) {
            stock = new Stock(buyOfferOwner,
                    buyOffer.getCompany(), sellOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + sellOffer.getAmount());
        }
        buyOffer.setAmount(0);
        buyOffer.setActual(false);
        sellOffer.setAmount(0);
        sellOffer.setActual(false);
        dbTime = System.currentTimeMillis();
        stockRepository.save(stock);
        transactionRepository.save(transaction);
        buyOfferRepository.save(buyOffer);
        sellOfferRepository.save(sellOffer);
        databaseTime += System.currentTimeMillis() - dbTime;
        numberOfBuyOffers++;
        numberOfSellOffers++;
        return transaction;
    }

    public Transaction buyOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue()) / 2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, sellOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(sellOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * sellOffer.getAmount())));
        long dbTime = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        databaseTime += System.currentTimeMillis() - dbTime;
        Stock stock;
        if (!stockOptional.isPresent()) {
            stock = new Stock(buyOfferOwner, buyOffer.getCompany(), sellOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + sellOffer.getAmount());
        }
        buyOffer.setAmount(buyOffer.getAmount() - sellOffer.getAmount());
        sellOffer.setAmount(0);
        sellOffer.setActual(false);
        dbTime = System.currentTimeMillis();
        stockRepository.save(stock);
        transactionRepository.save(transaction);
        buyOfferRepository.save(buyOffer);
        sellOfferRepository.save(sellOffer);
        databaseTime += System.currentTimeMillis() - dbTime;
        numberOfSellOffers++;
        return transaction;
    }

    public Transaction sellOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue()) / 2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, buyOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(buyOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * buyOffer.getAmount())));
        long dbTime = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        databaseTime += System.currentTimeMillis() - dbTime;
        Stock stock;
        if (!stockOptional.isPresent()) {
            stock = new Stock(buyOfferOwner, buyOffer.getCompany(), buyOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + buyOffer.getAmount());
        }
        sellOffer.setAmount(sellOffer.getAmount() - buyOffer.getAmount());
        buyOffer.setAmount(0);
        buyOffer.setActual(false);
        dbTime = System.currentTimeMillis();
        stockRepository.save(stock);
        transactionRepository.save(transaction);
        sellOfferRepository.save(sellOffer);
        buyOfferRepository.save(buyOffer);
        databaseTime += System.currentTimeMillis() - dbTime;
        numberOfBuyOffers++;
        return transaction;
    }

    void updateStockRates(int companyId, List<Transaction> transactions) {
        double price = 0;
        int amount = 0;
        for (Transaction transaction : transactions) {
            price += transaction.getPrice() * transaction.getAmount();
            amount += transaction.getAmount();
        }

        double average = price / amount;
        long dbTime = System.currentTimeMillis();
        Optional<StockRate> stockRateOptional = stockRateRepository.findByCompany_IdAndActual(companyId, true);
        databaseTime += System.currentTimeMillis() - dbTime;

        if (stockRateOptional.isPresent()) {
            StockRate stockRate = stockRateOptional.get();
            double dif = average - stockRate.getRate();
            dif = dif / 10;
            double newPrice = stockRate.getRate() + dif;
            newPrice = Math.round(newPrice * 100) / 100.0;
            StockRate newStockRate = new StockRate(0, stockRate.getCompany(), newPrice, new Date(), true);
            stockRate.setActual(false);
            dbTime = System.currentTimeMillis();
            stockRateRepository.save(stockRate);
            stockRateRepository.save(newStockRate);
            databaseTime += System.currentTimeMillis() - dbTime;
        }

    }

    static class SortBuyOffers implements Comparator<BuyOffer> {
        public int compare(BuyOffer a, BuyOffer b) {
            return b.getMaxPrice().compareTo(a.getMaxPrice());
        }
    }

    static class SortSellOffers implements Comparator<SellOffer> {
        public int compare(SellOffer a, SellOffer b) {
            return a.getMinPrice().compareTo(b.getMinPrice());
        }
    }

}
