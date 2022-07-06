package com.raphau.springboot.stockExchange.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SchemaHandler {
    private final String SCHEMA_SQL = "classpath:clean.sql";
    @Autowired
    private DataSource datasource;
    @Autowired
    private SpringContextGetter springContextGetter;

    public void execute() throws Exception {
        System.out.println("1");
        Resource resource = springContextGetter.getApplicationContext().getResource(SCHEMA_SQL);
        System.out.println("2");
        ScriptUtils.executeSqlScript(datasource.getConnection(), resource);
        System.out.println("3");
    }
}
