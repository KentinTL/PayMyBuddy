package com.openclassrooms.paymybuddy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import java.sql.SQLException;

@Configuration
public class DataSqlRunner implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataSqlRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Resource resource = new ClassPathResource("data.sql");
            ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), resource);
            System.out.println("Data.sql executed successfully!");
        } catch (SQLException e) {
            System.out.println("Error while executing data.sql: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


