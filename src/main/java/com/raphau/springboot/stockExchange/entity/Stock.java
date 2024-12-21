package com.raphau.springboot.stockExchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="stock")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(targetEntity = Company.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SellOffer> sellOffers;

    @Column(name = "amount")
    private int amount;

    public Stock(int id, User user, Company company, int amount) {
        this.id = id;
        this.user = user;
        this.company = company;
        this.amount = amount;
    }
}
