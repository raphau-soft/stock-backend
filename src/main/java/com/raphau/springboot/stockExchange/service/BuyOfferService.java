package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.*;
import com.raphau.springboot.stockExchange.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
public class BuyOfferService {

    @Autowired
    private UserService userService;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<BuyOffer> getUserBuyOffers() {
        String username = AuthUtils.getAuthenticatedUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));

        return user.getBuyOffers().stream()
                .filter(BuyOffer::isActual)
                .toList();
    }

    public void deleteBuyOffer(int id) {
        String username = AuthUtils.getAuthenticatedUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));

        BuyOffer buyOffer = buyOfferRepository.findById(id)
                .orElseThrow(() -> new BuyOfferNotFoundException("Buy offer with id " + id + " not found"));

        if (user.getId() != buyOffer.getUser().getId()) {
            throw new UnauthorizedAccessException("User not authorized to delete this buy offer");
        }

        BigDecimal refund = buyOffer.getMaxPrice().multiply(BigDecimal.valueOf(buyOffer.getAmount()));
        user.setMoney(user.getMoney().add(refund));

        buyOffer.setActual(false);
        buyOfferRepository.save(buyOffer);
    }

    @Transactional
    public void addOffer(BuyOfferDTO buyOfferDTO) {
        String username = AuthUtils.getAuthenticatedUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        Company company = companyRepository.findById(buyOfferDTO.getCompany_id())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found: " + buyOfferDTO.getCompany_id()));

        BigDecimal totalCost = buyOfferDTO.getMaxPrice().multiply(BigDecimal.valueOf(buyOfferDTO.getAmount()));
        if (totalCost.compareTo(user.getMoney()) > 0 || buyOfferDTO.getAmount() <= 0) {
            throw new InsufficientFundsException("User" + username + " does not have enough money or invalid amount.");
        }

        user.setMoney(user.getMoney().subtract(totalCost));

        BuyOffer buyOffer = BuyOffer.builder()
                .maxPrice(buyOfferDTO.getMaxPrice())
                .amount(buyOfferDTO.getAmount())
                .dateLimit(new Date())
                .user(user)
                .company(company)
                .actual(true)
                .build();

        userRepository.save(user);
        buyOfferRepository.save(buyOffer);
    }

}
