package com.raphau.springboot.stockExchange.exception;

public class BuyOfferNotFoundException extends RuntimeException {
    public BuyOfferNotFoundException(String message) {
        super(message);
    }

    public BuyOfferNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuyOfferNotFoundException(Throwable cause) {
        super(cause);
    }
}
