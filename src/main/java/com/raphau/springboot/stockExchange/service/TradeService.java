package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.*;
import com.raphau.springboot.stockExchange.entity.*;
import com.raphau.springboot.stockExchange.exception.StockRateNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TradeService {

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

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void trade() {
        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            executeTradingForCompany(company);
        }
    }

    private void executeTradingForCompany(Company company) {
        List<Transaction> transactions = new ArrayList<>();
        List<BuyOffer> buyOffers = fetchSortedBuyOffers(company);
        List<SellOffer> sellOffers = fetchSortedSellOffers(company);

        while (!buyOffers.isEmpty() && !sellOffers.isEmpty()) {
            if (!processTradingCycle(buyOffers, sellOffers, transactions)) {
                break;
            }
            buyOffers = fetchSortedBuyOffers(company);
            sellOffers = fetchSortedSellOffers(company);
        }

        if (!transactions.isEmpty()) {
            updateStockRates(company.getId(), transactions);
        }
    }

    private List<BuyOffer> fetchSortedBuyOffers(Company company) {
        List<BuyOffer> buyOffers = new ArrayList<>(buyOfferRepository
                .findTop10ByCompany_IdAndActualOrderByMaxPriceDesc(company.getId(), true));
        buyOffers.sort(new SortBuyOffers());
        return buyOffers;
    }

    private List<SellOffer> fetchSortedSellOffers(Company company) {
        List<Stock> stocks = stockRepository.findByCompany_Id(company.getId());
        List<SellOffer> sellOffers = new ArrayList<>();
        stocks.forEach(stock -> sellOffers.addAll(stock.getSellOffers()));
        sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
        sellOffers.sort(new SortSellOffers());
        return sellOffers;
    }

    private boolean processTradingCycle(List<BuyOffer> buyOffers, List<SellOffer> sellOffers, List<Transaction> transactions) {
        return startTrading(buyOffers, sellOffers, transactions);
    }

    private boolean startTrading(List<BuyOffer> buyOffers, List<SellOffer> sellOffers, List<Transaction> transactions) {
        int buyIndex = 0, sellIndex = 0;

        while (buyIndex < buyOffers.size() && sellIndex < sellOffers.size()) {
            BuyOffer buyOffer = buyOffers.get(buyIndex);
            SellOffer sellOffer = sellOffers.get(sellIndex);

            if (!canTrade(buyOffer, sellOffer)) {
                return false;
            }

            if (buyOffer.getAmount() > sellOffer.getAmount()) {
                transactions.add(processBuyOfferStay(buyOffer, sellOffer));
                sellIndex++;
            } else if (buyOffer.getAmount() < sellOffer.getAmount()) {
                transactions.add(processSellOfferStay(buyOffer, sellOffer));
                buyIndex++;
            } else {
                transactions.add(processNoneOfferStay(buyOffer, sellOffer));
                buyIndex++;
                sellIndex++;
            }
        }

        return true;
    }

    private boolean canTrade(BuyOffer buyOffer, SellOffer sellOffer) {
        return buyOffer.getMaxPrice().compareTo(sellOffer.getMinPrice()) >= 0;
    }

    private Transaction processBuyOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        return buyOfferStay(buyOffer, sellOffer);
    }

    private Transaction processSellOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        return sellOfferStay(buyOffer, sellOffer);
    }

    private Transaction processNoneOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        return noneOfferStay(buyOffer, sellOffer);
    }

    private Transaction processTransaction(BuyOffer buyOffer, SellOffer sellOffer, int transactionAmount) {
        double price = calculateTransactionPrice(buyOffer, sellOffer);
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, transactionAmount, price, new Date());

        User sellOfferOwner = sellOffer.getUser();
        User buyOfferOwner = buyOffer.getUser();

        updateUserBalances(buyOfferOwner, sellOfferOwner, price, transactionAmount, buyOffer.getMaxPrice());

        Stock stock = updateBuyerStock(buyOfferOwner, buyOffer.getCompany(), transactionAmount);

        updateOfferAmounts(buyOffer, sellOffer, transactionAmount);

        saveTransaction(transaction, buyOffer, sellOffer, stock);

        return transaction;
    }

    private double calculateTransactionPrice(BuyOffer buyOffer, SellOffer sellOffer) {
        return (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue()) / 2;
    }

    private void updateUserBalances(User buyOfferOwner, User sellOfferOwner, double price, int amount, BigDecimal maxPrice) {
        BigDecimal priceDifference = maxPrice.subtract(BigDecimal.valueOf(price));
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(priceDifference.multiply(BigDecimal.valueOf(amount))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(BigDecimal.valueOf(price * amount)));
    }

    private Stock updateBuyerStock(User buyer, Company company, int transactionAmount) {
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(company, buyer);

        Stock stock;
        if (stockOptional.isEmpty()) {
            stock = new Stock(buyer, company, transactionAmount);
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + transactionAmount);
        }
        return stock;
    }

    private void updateOfferAmounts(BuyOffer buyOffer, SellOffer sellOffer, int transactionAmount) {
        buyOffer.setAmount(buyOffer.getAmount() - transactionAmount);
        sellOffer.setAmount(sellOffer.getAmount() - transactionAmount);

        if (buyOffer.getAmount() <= 0) {
            buyOffer.setActual(false);
        }
        if (sellOffer.getAmount() <= 0) {
            sellOffer.setActual(false);
        }
    }

    private void saveTransaction(Transaction transaction, BuyOffer buyOffer, SellOffer sellOffer, Stock stock) {
        stockRepository.save(stock);
        transactionRepository.save(transaction);
        buyOfferRepository.save(buyOffer);
        sellOfferRepository.save(sellOffer);
    }

    public Transaction noneOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        return processTransaction(buyOffer, sellOffer, sellOffer.getAmount());
    }

    public Transaction buyOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        return processTransaction(buyOffer, sellOffer, sellOffer.getAmount());
    }

    public Transaction sellOfferStay(BuyOffer buyOffer, SellOffer sellOffer) {
        return processTransaction(buyOffer, sellOffer, buyOffer.getAmount());
    }

    private void updateStockRates(int companyId, List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("Transaction list cannot be null or empty.");
        }

        double averagePrice = calculateAveragePrice(transactions);
        Optional<StockRate> currentStockRateOpt = stockRateRepository.findByCompany_IdAndActual(companyId, true);

        if (currentStockRateOpt.isPresent()) {
            StockRate currentStockRate = currentStockRateOpt.get();
            double adjustedPrice = calculateAdjustedPrice(averagePrice, currentStockRate.getRate());

            currentStockRate.setActual(false);

            StockRate newStockRate = new StockRate(0, currentStockRate.getCompany(), adjustedPrice, new Date(), true);

            stockRateRepository.save(currentStockRate);
            stockRateRepository.save(newStockRate);
        } else {
            throw new StockRateNotFoundException("No active stock rate found for company ID: " + companyId);
        }
    }

    private double calculateAveragePrice(List<Transaction> transactions) {
        double totalPrice = 0;
        int totalAmount = 0;

        for (Transaction transaction : transactions) {
            totalPrice += transaction.getPrice() * transaction.getAmount();
            totalAmount += transaction.getAmount();
        }

        if (totalAmount == 0) {
            throw new ArithmeticException("Total transaction amount cannot be zero.");
        }

        return totalPrice / totalAmount;
    }

    private double calculateAdjustedPrice(double averagePrice, double currentPrice) {
        double difference = (averagePrice - currentPrice) / 10;
        double adjustedPrice = currentPrice + difference;

        return Math.round(adjustedPrice * 100) / 100.0;
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
