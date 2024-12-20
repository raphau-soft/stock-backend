package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.BuyOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyOfferRepository extends JpaRepository<BuyOffer, Integer> {
    List<BuyOffer> findTop10ByCompany_IdAndActualOrderByMaxPriceDesc(int company_id, Boolean actual);
}
