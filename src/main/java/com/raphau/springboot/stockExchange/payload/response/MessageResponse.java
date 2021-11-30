package com.raphau.springboot.stockExchange.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse {
    @JsonProperty
    String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
