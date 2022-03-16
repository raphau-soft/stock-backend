package com.raphau.springboot.stockExchange.service.ints;

public interface TradeService {

	public void trade() throws InterruptedException;
	public void clearDB() throws InterruptedException;
	public void measure();
	public void addStocks();
	
}
