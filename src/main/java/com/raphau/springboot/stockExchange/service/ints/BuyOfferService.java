package com.raphau.springboot.stockExchange.service.ints;

import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;

import java.util.Map;

public interface BuyOfferService {

    Map<String, Object> getUserBuyOffers();
    TestDetailsDTO deleteBuyOffer(int theId);
    TestDetailsDTO addOffer(BuyOfferDTO buyOfferDTO) throws InterruptedException;

}
