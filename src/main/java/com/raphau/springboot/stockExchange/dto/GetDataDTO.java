package com.raphau.springboot.stockExchange.dto;

public class GetDataDTO  {
    private String username;
    private long timeDataId;

    public GetDataDTO() {
    }

    public GetDataDTO(String username, long timeDataId) {
        this.username = username;
        this.timeDataId = timeDataId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimeDataId() {
        return timeDataId;
    }

    public void setTimeDataId(long timeDataId) {
        this.timeDataId = timeDataId;
    }
}
