package com.raphau.springboot.stockExchange.exception;

public class StockRateNotFoundException extends NotFoundException {
    public StockRateNotFoundException(String message) {
        super(message);
    }
}
