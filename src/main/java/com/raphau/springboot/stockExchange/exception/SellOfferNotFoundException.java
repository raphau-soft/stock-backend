package com.raphau.springboot.stockExchange.exception;

public class SellOfferNotFoundException extends NotFoundException {
    public SellOfferNotFoundException(String message) {
        super(message);
    }
}
