package com.raphau.springboot.stockExchange.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

public class TimeDataDTO {

    private long timestamp;

    private long databaseTime;

    private long applicationTime;

    public TimeDataDTO() {
        super();
    }

    public TimeDataDTO(long timestamp, long databaseTime, long applicationTime) {
        this.timestamp = timestamp;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (applicationTime ^ (applicationTime >>> 32));
        result = prime * result + (int) (databaseTime ^ (databaseTime >>> 32));
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimeDataDTO other = (TimeDataDTO) obj;
        if (applicationTime != other.applicationTime)
            return false;
        if (databaseTime != other.databaseTime)
            return false;
        if (timestamp != other.timestamp)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TimeData [timestamp=" + timestamp + ", databaseTime=" + databaseTime
                + ", applicationTime=" + applicationTime + ", semaphoreWaitTime=" + "]";
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