package com.raphau.springboot.stockExchange.service.api;

public interface TradeService {

	public void trade() throws InterruptedException;
	public void addStocks();
	public void finishTrading(boolean finish);
	public void sendTradingConfirmation();

}
