package com.raphau.springboot.stockExchange.dto;

public class TestDetailsDTO {

    private long databaseTime;
    private long applicationTime;

    public TestDetailsDTO() {
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
}
