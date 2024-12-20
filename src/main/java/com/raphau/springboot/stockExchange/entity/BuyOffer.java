package com.raphau.springboot.stockExchange.entity;

import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="buy_offer", schema = "stock_exchange")
public class BuyOffer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = Company.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "buyOffer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Transaction> transactions;

    @Column(name="max_price")
    private BigDecimal maxPrice;

    @Column(name="start_amount")
    private int startAmount;

    @Column(name="amount")
    private int amount;

    @Column(name="date_limit")
    private Date dateLimit;

    @Column(name="actual")
    private boolean actual;

    public BuyOffer() {
    }

    public BuyOffer(int id, Company company, User user, BigDecimal maxPrice, int startAmount, int amount, Date dateLimit, boolean actual) {
        this.id = id;
        this.company = company;
        this.user = user;
        this.maxPrice = maxPrice;
        this.startAmount = startAmount;
        this.amount = amount;
        this.dateLimit = dateLimit;
        this.actual = actual;
    }
    
    public BuyOffer(BuyOfferDTO buyOfferDTO, User user, Company company) {
        this.id = buyOfferDTO.getId();
        this.company = company;
        this.user = user;
        this.maxPrice = buyOfferDTO.getMaxPrice();
        this.startAmount = buyOfferDTO.getAmount();
        this.amount = buyOfferDTO.getAmount();
        this.dateLimit = buyOfferDTO.getDateLimit();
        this.actual = true;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public BuyOffer setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public int getStartAmount() {
        return startAmount;
    }

    public BuyOffer setStartAmount(int startAmount) {
        this.startAmount = startAmount;
        return this;
    }

    public boolean isActual() {
        return actual;
    }

    public BuyOffer setActual(boolean actual) {
        this.actual = actual;
        return this;
    }

    public int getId() {
        return id;
    }

    public BuyOffer setId(int id) {
        this.id = id;
        return this;
    }

    public Company getCompany() {
        return company;
    }

    public BuyOffer setCompany(Company company) {
        this.company = company;
        return this;
    }

    public User getUser() {
        return user;
    }

    public BuyOffer setUser(User user) {
        this.user = user;
        return this;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public BuyOffer setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public BuyOffer setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public Date getDateLimit() {
        return dateLimit;
    }

    public BuyOffer setDateLimit(Date dateLimit) {
        this.dateLimit = dateLimit;
        return this;
    }

    @Override
    public String toString() {
        return "\n\nBuyOffer{" +
                "id=" + id +
                ", company=" + company.getId() +
                ", user=" + user.getId() +
                ", maxPrice=" + maxPrice +
                ", amount=" + amount +
                ", dateLimit=" + dateLimit +
                "}\n\n";
    }
}
