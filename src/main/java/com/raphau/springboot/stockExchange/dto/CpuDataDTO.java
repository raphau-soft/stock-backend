package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CpuDataDTO {
    private long timestamp;
    private Double cpuUsage;
	private Double memory;
	private String stockId;
}