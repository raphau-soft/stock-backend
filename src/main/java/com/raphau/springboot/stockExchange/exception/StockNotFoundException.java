package com.raphau.springboot.stockExchange.exception;

public class StockNotFoundException extends NotFoundException {
    public StockNotFoundException(String message) {
        super(message);
    }
}
