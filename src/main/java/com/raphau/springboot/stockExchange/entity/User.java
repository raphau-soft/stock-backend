package com.raphau.springboot.stockExchange.entity;

import com.raphau.springboot.stockExchange.dto.UserDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="user")
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="money")
    private BigDecimal money;

    @Column(name="email")
    private String email;

    @Column(name="role")
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BuyOffer> buyOffers;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SellOffer> sellOffers;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Stock> stocks;

    public User(int id, String name, String surname, String username, String password, BigDecimal money, String email, String role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.money = money;
        this.email = email;
        this.role = role;
    }
}