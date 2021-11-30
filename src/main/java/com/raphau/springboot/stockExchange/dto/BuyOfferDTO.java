package com.raphau.springboot.stockExchange.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BuyOfferDTO {

    private int id;
    private int company_id;
    private BigDecimal maxPrice;
    private int amount;
    private Date dateLimit;

    public BuyOfferDTO() {
    }

    public BuyOfferDTO(int id, int company_id, BigDecimal maxPrice, int amount, Date dateLimit) {
        this.id = id;
        this.company_id = company_id;
        this.maxPrice = maxPrice;
        this.amount = amount;
        this.dateLimit = dateLimit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(Date dateLimit) {
        this.dateLimit = dateLimit;
    }
}
