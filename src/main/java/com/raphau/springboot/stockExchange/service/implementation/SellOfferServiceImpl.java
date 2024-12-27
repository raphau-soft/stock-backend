package com.raphau.springboot.stockExchange.service.implementation;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.*;
import com.raphau.springboot.stockExchange.service.api.SellOfferService;
import com.raphau.springboot.stockExchange.service.api.UserService;
import com.raphau.springboot.stockExchange.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellOfferServiceImpl implements SellOfferService {

    @Autowired
    private UserService userService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<SellOffer> getUserSellOffers() {
        String username = AuthUtils.getAuthenticatedUsername();
        User user = getUserByUsername(username);

        return user.getSellOffers().stream()
                .filter(SellOffer::isActual)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSellOffer(int id) {
        String username = AuthUtils.getAuthenticatedUsername();
        User user = getUserByUsername(username);
        SellOffer sellOffer = getSellOfferById(id);

        Stock stock = sellOffer.getStock();

        if (user.getId() != stock.getUser().getId()) {
            throw new UnauthorizedAccessException("User not authorized to delete this sell offer");
        }

        stock.setAmount(stock.getAmount() + sellOffer.getAmount());
        sellOffer.setActual(false);

        stockRepository.save(stock);
        sellOfferRepository.save(sellOffer);
    }

    @Override
    public void addSellOffer(SellOfferDTO sellOfferDTO) {
        Company company = getCompanyById(sellOfferDTO.getCompany_id());
        User user = getUserByUsername(sellOfferDTO.getUsername());
        Stock stock = getStockByCompanyAndUser(company, user);

        validateSellAmount(stock, sellOfferDTO.getAmount());

        stock.setAmount(stock.getAmount() - sellOfferDTO.getAmount());
        SellOffer sellOffer = new SellOffer(sellOfferDTO, user, stock);

        stockRepository.save(stock);
        sellOfferRepository.save(sellOffer);
    }

    private Company getCompanyById(int companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company with ID " + companyId + " not found"));
    }

    private SellOffer getSellOfferById(int sellOfferId) {
        return sellOfferRepository.findById(sellOfferId)
                .orElseThrow(() -> new SellOfferNotFoundException("Sell offer with ID " + sellOfferId + " not found"));
    }

    private User getUserByUsername(String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    private Stock getStockByCompanyAndUser(Company company, User user) {
        return stockRepository.findByCompanyAndUser(company, user)
                .orElseThrow(() -> new StockNotFoundException("Stock not found for user " + user.getUsername() + " and company " + company.getName()));
    }

    private void validateSellAmount(Stock stock, int amountToSell) {
        if (amountToSell > stock.getAmount()) {
            throw new InsufficientStockException("Insufficient stock. You only have " + stock.getAmount() + " shares.");
        }
    }


}
