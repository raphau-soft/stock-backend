package com.raphau.springboot.stockExchange.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="stock")
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

    @OneToMany(mappedBy = "stock", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<SellOffer> sellOffers;

    @Column(name = "amount")
    private int amount;

    public Stock() {
    }

    public Stock(int id, User user, Company company, int amount) {
        this.id = id;
        this.user = user;
        this.company = company;
        this.amount = amount;
    }

    public List<SellOffer> getSellOffers() {
        return sellOffers;
    }

    public void setSellOffers(List<SellOffer> sellOffers) {
        this.sellOffers = sellOffers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", user=" + user +
                ", company=" + company +
                ", sellOffers=" + sellOffers +
                ", amount=" + amount +
                '}';
    }
}
