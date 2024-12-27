package com.raphau.springboot.stockExchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="stock_rate")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = Company.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

    @Column(name="rate")
    private double rate;

    @Column(name="date_inc")
    private Date date;

    @Column(name="actual")
    private boolean actual;

    public StockRate(Company company, double rate, Date date, boolean actual) {
        this.company = company;
        this.rate = rate;
        this.date = date;
        this.actual = actual;
    }
}
