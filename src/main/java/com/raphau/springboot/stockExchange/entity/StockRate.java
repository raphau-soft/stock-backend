package com.raphau.springboot.stockExchange.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="stock_rate")
public class StockRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    // company
    @ManyToOne(targetEntity = Company.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

    @Column(name="rate")
    private double rate;

    @Column(name="date_inc")
    private Date date;

    @Column(name="actual")
    private boolean actual;

    public StockRate() {
    }

    public StockRate(int id, Company company, double rate, Date date, boolean actual) {
        this.id = id;
        this.company = company;
        this.rate = rate;
        this.date = date;
        this.actual = actual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    @Override
    public String toString() {
        return "StockRate{" +
                "id=" + id +
                ", company=" + company +
                ", rate=" + rate +
                ", date=" + date +
                ", actual=" + actual +
                '}';
    }
}
