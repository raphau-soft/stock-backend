package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellOfferRepository extends JpaRepository<SellOffer, Integer> {

    Page<SellOffer> findAllByUserId(Pageable pageable, int userId);
    List<SellOffer> findTop10ByStockAndActualOrderByMinPriceAsc(Stock stock, Boolean actual);

}
