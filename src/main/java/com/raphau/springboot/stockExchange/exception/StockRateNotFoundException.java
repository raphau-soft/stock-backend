package com.raphau.springboot.stockExchange.exception;

public class StockRateNotFoundException extends RuntimeException {
    public StockRateNotFoundException(String message) {
        super(message);
    }

    public StockRateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockRateNotFoundException(Throwable cause) {
        super(cause);
    }
}
