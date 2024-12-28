package com.raphau.springboot.stockExchange.exception;

public class BuyOfferNotFoundException extends NotFoundException {
    public BuyOfferNotFoundException(String message) {
        super(message);
    }
}
