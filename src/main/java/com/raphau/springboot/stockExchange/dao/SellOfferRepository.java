package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.SellOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellOfferRepository extends JpaRepository<SellOffer, Integer> {

}
