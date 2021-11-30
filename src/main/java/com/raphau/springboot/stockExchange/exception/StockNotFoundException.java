package com.raphau.springboot.stockExchange.exception;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message) {
        super(message);
    }

    public StockNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockNotFoundException(Throwable cause) {
        super(cause);
    }
}
