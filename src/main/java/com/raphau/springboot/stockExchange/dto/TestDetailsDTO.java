package com.raphau.springboot.stockExchange.dto;

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

    public TestDetailsDTO(long id, long databaseTime, long applicationTime, long timestamp, int queueSizeBack, String endpointUrl, String stockId) {
        this.id = id;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
        this.timestamp = timestamp;
        this.queueSizeBack = queueSizeBack;
        this.endpointUrl = endpointUrl;
        this.stockId = stockId;
    }

    public int getQueueSizeBack() {
        return queueSizeBack;
    }

    public void setQueueSizeBack(int queueSizeBack) {
        this.queueSizeBack = queueSizeBack;
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
