package com.raphau.springboot.stockExchange.service.api;

public interface TradeService {
	void trade() throws InterruptedException;
	void addStocks();
	void finishTrading(boolean finish);
}
