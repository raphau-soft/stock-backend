package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    private static final Logger log = LoggerFactory.getLogger(ViewController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    @GetMapping("/index")
    public String index(@RequestParam(value = "page", defaultValue = "1") String page, @RequestParam(value = "size", defaultValue = "10") String size, Model model) {
        List<User> users;
        int currentPage = Integer.parseInt(page) - 1;
        int pageSize = Integer.parseInt(size);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<User> usersPage = userRepository.findAll(pageable);
        users = usersPage.getContent();
        model.addAttribute("users", users);
        model.addAttribute("currentPage", usersPage.getNumber() + 1);
        model.addAttribute("totalItems", usersPage.getTotalElements());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("currentSize", size);
        return "index";
    }

    @GetMapping("/user/buyoffers")
    public String buyOffers(@RequestParam(value = "page", defaultValue = "1") String page, @RequestParam(value = "size", defaultValue = "10") String size, @RequestParam(value = "id", required = true) String id, Model model) {
        List<BuyOffer> buyOffers;
        int currentPage = Integer.parseInt(page) - 1;
        int pageSize = Integer.parseInt(size);
        int userId = Integer.parseInt(id);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<BuyOffer> buyOffersPage = buyOfferRepository.findAllByUserId(pageable, userId);
        buyOffers = buyOffersPage.getContent();
        model.addAttribute("buyOffers", buyOffers);
        model.addAttribute("currentPage", buyOffersPage.getNumber() + 1);
        model.addAttribute("totalItems", buyOffersPage.getTotalElements());
        model.addAttribute("totalPages", buyOffersPage.getTotalPages());
        model.addAttribute("currentSize", size);
        model.addAttribute("userId", userId);
        return "buyOffers";
    }

    @GetMapping("/user/selloffers")
    public String sellOffers(@RequestParam(value = "page", defaultValue = "1") String page, @RequestParam(value = "size", defaultValue = "10") String size, @RequestParam(value = "id", required = true) String id, Model model) {
        List<SellOffer> sellOffers;
        int currentPage = Integer.parseInt(page) - 1;
        int pageSize = Integer.parseInt(size);
        int userId = Integer.parseInt(id);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<SellOffer> sellOfferPage = sellOfferRepository.findAllByUserId(pageable, userId);
        sellOffers = sellOfferPage.getContent();
        model.addAttribute("sellOffers", sellOffers);
        model.addAttribute("currentPage", sellOfferPage.getNumber() + 1);
        model.addAttribute("totalItems", sellOfferPage.getTotalElements());
        model.addAttribute("totalPages", sellOfferPage.getTotalPages());
        model.addAttribute("currentSize", size);
        model.addAttribute("userId", userId);
        return "sellOffers";
    }

}
