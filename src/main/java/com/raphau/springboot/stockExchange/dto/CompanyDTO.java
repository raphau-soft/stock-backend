package com.raphau.springboot.stockExchange.dto;

public class CompanyDTO {

    private int id;
    private String username;
    private String name;
    private int amount;
    private double price;
    private long timeDataId;

    public CompanyDTO(int id, String username, String name, int amount, double price, long timeDataId) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.timeDataId = timeDataId;
    }

    public long getTimeDataId() {
        return timeDataId;
    }

    public void setTimeDataId(long timeDataId) {
        this.timeDataId = timeDataId;
    }

    public CompanyDTO() {
    }

    public CompanyDTO(String name, int amount, double price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public CompanyDTO(int id, String username, String name, int amount, double price) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
