package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellOfferRepository extends JpaRepository<SellOffer, Integer> {

    List<SellOffer> findByStockAndActual(Stock stock, Boolean actual);

}
