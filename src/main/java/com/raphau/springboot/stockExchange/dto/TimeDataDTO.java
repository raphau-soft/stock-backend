package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeDataDTO {
    private long id;
    private long timestamp;
    private long databaseTime;
    private long applicationTime;
    private long numberOfSellOffers;
    private long numberOfBuyOffers;
    private String stockId;

    public TimeDataDTO(long timestamp, long databaseTime, long applicationTime, String stockId) {
        this.timestamp = timestamp;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
        this.stockId = stockId;
    }
}