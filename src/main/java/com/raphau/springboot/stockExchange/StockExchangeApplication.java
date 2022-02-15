package com.raphau.springboot.stockExchange;

import com.raphau.springboot.stockExchange.dao.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableScheduling
public class StockExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockExchangeApplication.class, args);
	}

}
