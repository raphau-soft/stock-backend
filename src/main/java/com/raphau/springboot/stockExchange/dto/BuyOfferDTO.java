package com.raphau.springboot.stockExchange.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BuyOfferDTO {

    @NotNull(message = "Company ID cannot be null")
    @Min(value = 1, message = "Company ID must be a positive integer")
    private int company_id;

    @NotNull(message = "Max price cannot be null")
    @DecimalMin(value = "0.01", message = "Max price must be greater than 0")
    @DecimalMax(value = "1000000.00", message = "Max price cannot be greater than 1,000,000")
    private BigDecimal maxPrice;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 1, message = "Amount must be at least 1")
    @Max(value = 10000, message = "Amount cannot be greater than 10,000")
    private int amount;

    @NotNull(message = "Date limit cannot be null")
    private Date dateLimit;
}
