package com.raphau.springboot.stockExchange.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

public class CpuDataDTO {

    private long timestamp;

    private Double cpuUsage;

	private String stockId;

    public CpuDataDTO() {
    }

	public CpuDataDTO(long timestamp, Double cpuUsage, String stockId) {
		this.timestamp = timestamp;
		this.cpuUsage = cpuUsage;
		this.stockId = stockId;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Double getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	@Override
	public String toString() {
		return "CpuData [timestamp=" + timestamp + ", cpuUsage=" + cpuUsage + "]";
	}


}