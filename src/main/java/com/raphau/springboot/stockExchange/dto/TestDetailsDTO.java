package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TestDetailsDTO {

    private long id;
    private long databaseTime;
    private long applicationTime;
    private long timestamp;
    private int queueSizeBack;
    private String endpointUrl;
    private String stockId;

    public TestDetailsDTO(long id, long databaseTime, long applicationTime, long timestamp, String endpointUrl, String stockId) {
        this.id = id;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
        this.timestamp = timestamp;
        this.endpointUrl = endpointUrl;
        this.stockId = stockId;
    }
}
