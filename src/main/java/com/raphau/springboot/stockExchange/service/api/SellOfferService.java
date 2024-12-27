package com.raphau.springboot.stockExchange.service.api;

import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.entity.SellOffer;

import java.util.List;
import java.util.Map;

public interface SellOfferService {
    List<SellOffer> getUserSellOffers();
    void deleteSellOffer(int theId);
    void addSellOffer(SellOfferDTO sellOfferDTO);
}
