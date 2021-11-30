package com.raphau.springboot.stockExchange.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="transaction")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = BuyOffer.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="buy_offer_id", nullable = false)
    private BuyOffer buyOffer;

    @ManyToOne(targetEntity = SellOffer.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="sell_offer_id", nullable = false)
    private SellOffer sellOffer;

    @Column(name="amount")
    private int amount;

    @Column(name = "price")
    private double price;

    @Column(name = "transaction_date")
    private Date date;

    public Transaction() {
    }

    public Transaction(int id, BuyOffer buyOffer, SellOffer sellOffer, int amount, double price, Date date) {
        this.id = id;
        this.buyOffer = buyOffer;
        this.sellOffer = sellOffer;
        this.amount = amount;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BuyOffer getBuyOffer() {
        return buyOffer;
    }

    public void setBuyOffer(BuyOffer buyOffer) {
        this.buyOffer = buyOffer;
    }

    public SellOffer getSellOffer() {
        return sellOffer;
    }

    public void setSellOffer(SellOffer sellOffer) {
        this.sellOffer = sellOffer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", buyOffer=" + buyOffer +
                ", sellOffer=" + sellOffer +
                ", amount=" + amount +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
