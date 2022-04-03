package com.raphau.springboot.stockExchange.dto;

import java.util.Objects;

public class TimeDataDTO {

    private long timestamp;

    private long databaseTime;

    private long applicationTime;

    private long numberOfSellOffers;

    private long numberOfBuyOffers;

    private long id;

    private String stockId;

    public TimeDataDTO(long timestamp, long databaseTime, long applicationTime, long numberOfSellOffers, long numberOfBuyOffers, long id) {
        this.timestamp = timestamp;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
        this.numberOfSellOffers = numberOfSellOffers;
        this.numberOfBuyOffers = numberOfBuyOffers;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TimeDataDTO() {
        super();
    }

    public TimeDataDTO(long timestamp, long databaseTime, long applicationTime, String stockId) {
        this.timestamp = timestamp;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
        this.stockId = stockId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public TimeDataDTO(long timestamp, long databaseTime, long applicationTime, long numberOfSellOffers, long numberOfBuyOffers) {
        this.timestamp = timestamp;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
        this.numberOfSellOffers = numberOfSellOffers;
        this.numberOfBuyOffers = numberOfBuyOffers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeDataDTO that = (TimeDataDTO) o;
        return timestamp == that.timestamp && databaseTime == that.databaseTime && applicationTime == that.applicationTime && numberOfSellOffers == that.numberOfSellOffers && numberOfBuyOffers == that.numberOfBuyOffers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, databaseTime, applicationTime, numberOfSellOffers, numberOfBuyOffers);
    }

    @Override
    public String toString() {
        return "TimeDataDTO{" +
                "timestamp=" + timestamp +
                ", databaseTime=" + databaseTime +
                ", applicationTime=" + applicationTime +
                ", numberOfSellOffers=" + numberOfSellOffers +
                ", numberOfBuyOffers=" + numberOfBuyOffers +
                '}';
    }

    public long getNumberOfSellOffers() {
        return numberOfSellOffers;
    }

    public void setNumberOfSellOffers(long numberOfSellOffers) {
        this.numberOfSellOffers = numberOfSellOffers;
    }

    public long getNumberOfBuyOffers() {
        return numberOfBuyOffers;
    }

    public void setNumberOfBuyOffers(long numberOfBuyOffers) {
        this.numberOfBuyOffers = numberOfBuyOffers;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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