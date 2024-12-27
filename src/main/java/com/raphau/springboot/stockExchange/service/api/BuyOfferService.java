package com.raphau.springboot.stockExchange.service.api;

import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.entity.BuyOffer;

import java.util.List;
import java.util.Map;

public interface BuyOfferService {
    List<BuyOffer> getUserBuyOffers();
    void deleteBuyOffer(int theId);
    void addOffer(BuyOfferDTO buyOfferDTO);
}
