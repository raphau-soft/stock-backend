package com.raphau.springboot.stockExchange.exception;

public class CompanyNotFoundException extends NotFoundException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
