package com.raphau.springboot.stockExchange.service;

import java.io.IOException;

public interface TradeService {

	public void trade() throws InterruptedException;
	public void measure() throws IOException, InterruptedException;
	public void addStocks();
	public void finishTrading(boolean finish);
	public void sendTradingConfirmation();

}
