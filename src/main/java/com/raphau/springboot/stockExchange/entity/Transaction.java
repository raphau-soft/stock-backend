package com.raphau.springboot.stockExchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="transaction")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = BuyOffer.class, fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name="buy_offer_id", nullable = false)
    private BuyOffer buyOffer;

    @ManyToOne(targetEntity = SellOffer.class, fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name="sell_offer_id", nullable = false)
    private SellOffer sellOffer;

    @Column(name="amount")
    private int amount;

    @Column(name = "price")
    private double price;

    @Column(name = "transaction_date")
    private Date date;
}
