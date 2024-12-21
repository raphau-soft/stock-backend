package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuyOfferDTO {
    private int id;
    private String username;
    private int company_id;
    private BigDecimal maxPrice;
    private int amount;
    private Date dateLimit;
    private long timeDataId;
}
