package com.raphau.springboot.stockExchange.service.ints;

import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.dto.UserUpdDTO;
import com.raphau.springboot.stockExchange.entity.User;

import java.util.Map;
import java.util.Optional;

public interface UserService {

    Map<String, Object> getUserDetails();
    Optional<User> findByUsername(String username);
    TestDetailsDTO updateUser(UserUpdDTO userUpdDTO);

}
