package com.raphau.springboot.stockExchange.service.ints;

import java.io.IOException;

public interface TradeService {

	public void trade() throws InterruptedException;
	public void clearDB() throws InterruptedException;
	public void measure() throws IOException, InterruptedException;
	public void addStocks();
	
}
