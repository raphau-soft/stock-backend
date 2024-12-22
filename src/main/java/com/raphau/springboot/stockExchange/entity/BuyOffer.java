package com.raphau.springboot.stockExchange.entity;

import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="buy_offer")
@NoArgsConstructor
@AllArgsConstructor
@Data
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
}
