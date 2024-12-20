package com.raphau.springboot.stockExchange.entity;

import com.raphau.springboot.stockExchange.dto.SellOfferDTO;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="sell_offer", schema = "stock_exchange")
public class SellOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = Stock.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="stock_id", nullable = false)
    private Stock stock;
    
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "sellOffer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Transaction> transactions;

    @Column(name="start_amount")
    private int startAmount;

    @Column(name="amount")
    private int amount;

    @Column(name="min_price")
    private BigDecimal minPrice;

    @Column(name="date_limit")
    private Date dateLimit;

    @Column(name="actual")
    private boolean actual;

    public SellOffer() {
    }

    public SellOffer(int id, Stock stock, User user, int startAmount, int amount, BigDecimal minPrice, Date dateLimit, boolean actual) {
        this.id = id;
        this.stock = stock;
        this.user = user;
        this.startAmount = startAmount;
        this.amount = amount;
        this.minPrice = minPrice;
        this.dateLimit = dateLimit;
        this.actual = actual;
    }

    // TODO user
    public SellOffer(SellOfferDTO sellOfferDTO, User user, Stock stock) {
        this.id = sellOfferDTO.getId();
        this.stock = stock;
        this.user = user;
        this.startAmount = sellOfferDTO.getAmount();
        this.amount = sellOfferDTO.getAmount();
        this.minPrice = sellOfferDTO.getMinPrice();
        this.dateLimit = sellOfferDTO.getDateLimit();
        this.actual = true;
    }

    public User getUser() {
		return user;
	}

	public SellOffer setUser(User user) {
		this.user = user;
        return this;
	}

	public List<Transaction> getTransactions() {
        return transactions;
    }

    public SellOffer setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public int getStartAmount() {
        return startAmount;
    }

    public SellOffer setStartAmount(int startAmount) {
        this.startAmount = startAmount;
        return this;
    }

    public boolean isActual() {
        return actual;
    }

    public SellOffer setActual(boolean actual) {
        this.actual = actual;
        return this;
    }

    public int getId() {
        return id;
    }

    public SellOffer setId(int id) {
        this.id = id;
        return this;
    }

    public Stock getStock() {
        return stock;
    }

    public SellOffer setStock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public SellOffer setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public SellOffer setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public Date getDateLimit() {
        return dateLimit;
    }

    public SellOffer setDateLimit(Date dateLimit) {
        this.dateLimit = dateLimit;
        return this;
    }

    @Override
    public String toString() {
        return "SellOffer{" +
                "id=" + id +
                ", company=" + stock.getCompany().getId() +
                ", amount=" + amount +
                ", minPrice=" + minPrice +
                ", dateLimit=" + dateLimit +
                "}";
    }
}