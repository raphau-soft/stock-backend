package com.raphau.springboot.stockExchange.service.ints;

import com.raphau.springboot.stockExchange.entity.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public interface TestService {
    List<Test> getTest();
    void cleanTestDB();
    void restartDB() throws SQLException, FileNotFoundException;
}
