package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Modifying
    @Query(value = "update User u set u.money = :money where u.id = :id", nativeQuery = true)
    void updateUser(@Param("money") BigDecimal money, @Param("id") int id);

}
