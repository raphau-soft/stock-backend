package com.raphau.springboot.stockExchange.entity;

import com.raphau.springboot.stockExchange.dto.SellOfferDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="sell_offer", schema = "stock_exchange")
@NoArgsConstructor
@AllArgsConstructor
@Data
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
}