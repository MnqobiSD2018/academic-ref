package com.hit.academic_ref.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseMigrationConfig {

    @Bean
    CommandLineRunner ensureProjectDepartmentColumn(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();

                try (ResultSet columns = metaData.getColumns(null, null, "projects", "department")) {
                    if (columns.next()) {
                        return;
                    }
                }

                try (var statement = connection.createStatement()) {
                    statement.executeUpdate("ALTER TABLE projects ADD COLUMN department VARCHAR(100)");
                }
            }
        };
    }
}