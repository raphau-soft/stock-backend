package com.raphau.springboot.stockExchange.dto;

public class TestDetailsDTO {

    private long id;
    private long databaseTime;
    private long applicationTime;
    private long timestamp;
    private String endpointUrl;
    private String method;
    private String stockId;

    public TestDetailsDTO(long id, long databaseTime, long applicationTime, long timestamp, String endpointUrl, String method, String stockId) {
        this.id = id;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
        this.timestamp = timestamp;
        this.endpointUrl = endpointUrl;
        this.method = method;
        this.stockId = stockId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TestDetailsDTO() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getDatabaseTime() {
        return databaseTime;
    }

    public void setDatabaseTime(long databaseTime) {
        this.databaseTime = databaseTime;
    }

    public long getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(long applicationTime) {
        this.applicationTime = applicationTime;
    }

    @Override
    public String toString() {
        return "TestDetailsDTO{" +
                ", databaseTime=" + databaseTime +
                ", applicationTime=" + applicationTime +
                '}';
    }
}
