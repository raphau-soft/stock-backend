package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.CpuData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CpuDataRepository extends JpaRepository<CpuData, Integer> {
}
