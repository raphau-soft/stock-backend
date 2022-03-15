package com.raphau.springboot.stockExchange.dto;

public class TestDetailsDTO {

    private long databaseTime;
    private long applicationTime;
    private long timestamp;
    private String endpointUrl;
    private String method;

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
