package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

	Optional<Test> findByFinished(boolean finished);
	
}
