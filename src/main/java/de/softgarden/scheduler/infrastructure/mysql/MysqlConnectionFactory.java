package de.softgarden.scheduler.infrastructure.mysql;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Factory
public class MysqlConnectionFactory {
    @Singleton
    public Connection create(@Value("${softgarden.database.jdbc.read}") String jdbcWriteEndpoint) throws SQLException {
        return DriverManager.getConnection(jdbcWriteEndpoint);
    }
}
