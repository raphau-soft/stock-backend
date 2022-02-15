package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.TimeData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeDataRepository extends JpaRepository<TimeData, Integer> {
}
