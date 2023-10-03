package de.softgarden.scheduler.infrastructure.mysql;

import de.softgarden.scheduler.domain.Meeting;
import jakarta.inject.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Singleton
public class MysqlMeetingsRepository {
    private final Connection connection;

    MysqlMeetingsRepository(Connection connection) {
        this.connection = connection;
    }

    public void store(int computationId, Meeting meeting) throws SQLException {
        String sql = "INSERT INTO meetings (compute_id, participants, optimal_range) VALUES (?,?,?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, computationId);
        statement.setString(2, String.join(", ", meeting.participants()));
        statement.setString(3, meeting.range().describe());

        statement.execute();
    }
}
