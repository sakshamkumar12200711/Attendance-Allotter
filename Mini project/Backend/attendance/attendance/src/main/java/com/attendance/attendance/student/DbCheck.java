package com.attendance.attendance.student;

import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbCheck implements CommandLineRunner {

    private final DataSource dataSource;

    public DbCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (var conn = dataSource.getConnection()) {
            System.out.println("DB CONNECTED: " + conn.getCatalog());
        }
    }
}