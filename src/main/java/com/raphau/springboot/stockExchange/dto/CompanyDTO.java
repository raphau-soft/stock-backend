package com.raphau.springboot.stockExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyDTO {
    private int id;
    private String username;
    private String name;
    private int amount;
    private double price;
    private long timeDataId;
}
