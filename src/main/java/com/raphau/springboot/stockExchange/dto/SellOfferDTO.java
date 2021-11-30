package com.raphau.springboot.stockExchange.dto;

import java.math.BigDecimal;
import java.util.Date;

public class SellOfferDTO {

    private int id;
    private int company_id;
    private BigDecimal minPrice;
    private int amount;
    private Date dateLimit;

    public SellOfferDTO() {
    }

    public SellOfferDTO(int id, int company_id, BigDecimal minPrice, int amount, Date dateLimit) {
        this.id = id;
        this.company_id = company_id;
        this.minPrice = minPrice;
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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
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

    @Override
    public String toString() {
        return "SellOfferDTO{" +
                "id=" + id +
                ", company_id=" + company_id +
                ", maxPrice=" + minPrice +
                ", amount=" + amount +
                ", dateLimit=" + dateLimit +
                '}';
    }
}
