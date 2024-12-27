package com.raphau.springboot.stockExchange.exception;

public class SellOfferNotFoundException extends RuntimeException {
    public SellOfferNotFoundException(String message) {
        super(message);
    }

    public SellOfferNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SellOfferNotFoundException(Throwable cause) {
        super(cause);
    }
}
