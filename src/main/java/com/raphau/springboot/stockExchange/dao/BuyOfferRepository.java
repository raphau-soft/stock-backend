package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyOfferRepository extends JpaRepository<BuyOffer, Integer> {

    List<BuyOffer> findByUser(User user);
    List<BuyOffer> findByCompany_Id(int company_id);
    List<BuyOffer> findByCompany_IdAndActual(int company_id, Boolean actual);

}
