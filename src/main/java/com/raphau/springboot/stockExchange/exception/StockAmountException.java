package com.raphau.springboot.stockExchange.exception;

public class StockAmountException extends RuntimeException{
    public StockAmountException(String message) {
        super(message);
    }

    public StockAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockAmountException(Throwable cause) {
        super(cause);
    }
}
