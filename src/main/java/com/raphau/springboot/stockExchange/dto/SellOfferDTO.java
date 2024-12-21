package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SellOfferDTO {
    private int id;
    private String username;
    private int company_id;
    private BigDecimal minPrice;
    private int amount;
    private Date dateLimit;
    private long timeDataId;
}