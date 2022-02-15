package com.raphau.springboot.stockExchange.service.imps;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.CpuDataRepository;
import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dao.TestRepository;
import com.raphau.springboot.stockExchange.dao.TimeDataRepository;
import com.raphau.springboot.stockExchange.dao.TransactionRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.CpuData;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.Test;
import com.raphau.springboot.stockExchange.entity.TimeData;
import com.raphau.springboot.stockExchange.entity.Transaction;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.service.ints.TradeService;

@Service
public class TradeServiceImpl implements TradeService {

	private final int OFFERS_NUMBER = 5;
	private long databaseTime;
	public static Test test;
	public static boolean testStarted = false;
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
    private TimeDataRepository timeDataRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CpuDataRepository cpuDataRepository;
	

	@Override
	@Scheduled(cron = "0 */10 * * * ?")
	public void addStocks() {
		if(!testStarted) return;
		List<Stock> stocks = stockRepository.findAll();
		for(Stock stock: stocks) {
			stock.setAmount(stock.getAmount() + stock.getAmount() /10);
			stockRepository.save(stock);
		}
	}
    
	@Override
	@Scheduled(cron = "0 */1 * * * ?")
	public void measure() {
		if(!testStarted) return;
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		double percentMemoryUsage = ((double)memoryMXBean.getHeapMemoryUsage().getUsed() /1073741824)/((double)memoryMXBean.getHeapMemoryUsage().getInit() /1073741824);
		
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

                CpuData cpuData = new CpuData(0, test, System.currentTimeMillis(), (Double) value);
                cpuDataRepository.save(cpuData);
            }
        }
		
	}
    
	@Override
	@Scheduled(cron = "0 */5 * * * ?")
	public void trade() {
		if(!testStarted) return;
		long applicationTime = System.currentTimeMillis();
		databaseTime = 0;
		List<Company> companies = companyRepository.findAll();
		for(Company company : companies) {
			long dbTime = System.currentTimeMillis();
			List<BuyOffer> buyOffers = new ArrayList<>(buyOfferRepository
	                .findByCompany_IdAndActual(company.getId(), true));
			List<Stock> stocks = stockRepository.findByCompany_Id(company.getId());
			databaseTime += System.currentTimeMillis() - dbTime;
			List<SellOffer> sellOffers = new ArrayList<>();
	        stocks.forEach(stock -> sellOffers.addAll(stock.getSellOffers()));
	        sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
	        buyOffers.sort(new SortBuyOffers());
	        sellOffers.sort(new SortSellOffers());
	        List<Transaction> transactions = new ArrayList<>();
	        int i = 0;
            while(buyOffers.size() >= OFFERS_NUMBER
                    && sellOffers.size() >= OFFERS_NUMBER){
                i++;
                if(!startTrading(buyOffers.subList(0, OFFERS_NUMBER),
                        sellOffers.subList(0, OFFERS_NUMBER), transactions)){
                    break;
                }
                sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
                buyOffers.removeIf(buyOffer -> !buyOffer.isActual());
            }
            if(!transactions.isEmpty()) {
                updateStockRates(company.getId(), transactions);
            }
		}
		applicationTime = System.currentTimeMillis() - applicationTime;
		TimeData timeData = new TimeData(0, test, System.currentTimeMillis(), databaseTime, applicationTime);
		timeDataRepository.save(timeData);
	}
	
	private boolean startTrading(List<BuyOffer> buyOffers,
            	List<SellOffer> sellOffers, List<Transaction> transactions) {
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
					transactions.add(buyOfferStay(buyOffer, sellOffer));
					j++;
				} else if (buyOffer.getAmount() < sellOffer.getAmount()){
					transactions.add(sellOfferStay(buyOffer, sellOffer));
					i++;
				} else {
					transactions.add(noneOfferStay(buyOffer,sellOffer));
					i++;j++;
				}
			}
			return true;
	}

	private Transaction noneOfferStay(BuyOffer buyOffer, SellOffer sellOffer){
		double price = (buyOffer.getMaxPrice().doubleValue()
				+ sellOffer.getMinPrice().doubleValue())/2;
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
		if(!stockOptional.isPresent()) {
			stock = new Stock(0, buyOfferOwner,
            buyOffer.getCompany(), sellOffer.getAmount());
		} else {
			stock = stockOptional.get();
			stock.setAmount(stock.getAmount() + sellOffer.getAmount());
		}
		buyOffer.setAmount(0)
			.setActual(false);
		sellOffer.setAmount(0)
			.setActual(false);
		dbTime = System.currentTimeMillis();
		stockRepository.save(stock);
		transactionRepository.save(transaction);
		buyOfferRepository.save(buyOffer);
		sellOfferRepository.save(sellOffer);
		databaseTime += System.currentTimeMillis() - dbTime;
		return transaction;
	}

	private Transaction buyOfferStay(BuyOffer buyOffer, SellOffer sellOffer){
		double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
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
		if(!stockOptional.isPresent()) {
			stock = new Stock(0, buyOfferOwner, buyOffer.getCompany(), sellOffer.getAmount());
		} else {
			stock = stockOptional.get();
			stock.setAmount(stock.getAmount() + sellOffer.getAmount());
		}
		buyOffer.setAmount(buyOffer.getAmount() - sellOffer.getAmount());
		sellOffer.setAmount(0)
			.setActual(false);
		dbTime = System.currentTimeMillis();
		stockRepository.save(stock);
		transactionRepository.save(transaction);
		buyOfferRepository.save(buyOffer);
		sellOfferRepository.save(sellOffer);
		databaseTime += System.currentTimeMillis() - dbTime;
		return transaction;
	}
	
	private Transaction sellOfferStay(BuyOffer buyOffer, SellOffer sellOffer){
		double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
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
		if(!stockOptional.isPresent()) {
			stock = new Stock(0, buyOfferOwner, buyOffer.getCompany(), buyOffer.getAmount());
		} else {
			stock = stockOptional.get();
			stock.setAmount(stock.getAmount() + buyOffer.getAmount());
		}
		sellOffer.setAmount(sellOffer.getAmount() - buyOffer.getAmount());
		buyOffer.setAmount(0)
			.setActual(false);
		dbTime = System.currentTimeMillis();
		stockRepository.save(stock);
		transactionRepository.save(transaction);
		sellOfferRepository.save(sellOffer);
		buyOfferRepository.save(buyOffer);
		databaseTime += System.currentTimeMillis() - dbTime;
		return transaction;
	}
	
	private void updateStockRates(int companyId, List<Transaction> transactions){
        double price = 0;
        int amount = 0;
        for(Transaction transaction: transactions){
            price += transaction.getPrice() * transaction.getAmount();
            amount += transaction.getAmount();
        }

        double average = price / amount;
        long dbTime = System.currentTimeMillis();
        Optional<StockRate> stockRateOptional = stockRateRepository.findByCompany_IdAndActual(companyId, true);
        databaseTime += System.currentTimeMillis() - dbTime;

        if(stockRateOptional.isPresent()){
            StockRate stockRate = stockRateOptional.get();
            double dif = average - stockRate.getRate();
            dif = dif/10;
            double newPrice = stockRate.getRate() + dif;
            newPrice = Math.round(newPrice*100) / 100.0;
            StockRate newStockRate = new StockRate(0, stockRate.getCompany(), newPrice, new Date(), true);
            stockRate.setActual(false);
            dbTime = System.currentTimeMillis();
            stockRateRepository.save(stockRate);
            stockRateRepository.save(newStockRate);
            databaseTime += System.currentTimeMillis() - dbTime;
        }

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



























