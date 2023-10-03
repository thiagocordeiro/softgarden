package de.softgarden.scheduler.infrastructure.mysql;

import de.softgarden.scheduler.domain.Availability;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
class MysqlParticipantAvailabilitiesRepository {
    Connection connection;

    @Inject
    MysqlParticipantAvailabilitiesRepository(Connection connection) {
        this.connection = connection;
    }

    public void storeParticipantAvailability(int computationId, Availability availability) throws SQLException {
        List<String> bulkBindings = availability.times()
                .stream()
                .map(time -> "(?,?,?,?)")
                .toList();

        String sql = "INSERT INTO participant_availabilities " +
                "(compute_id, participant, available_at, created_at) " +
                "VALUES " + String.join(",", bulkBindings);

        PreparedStatement statement = connection.prepareStatement(sql);

        long createdAt = new Date().getTime();
        AtomicInteger jdbcIndex = new AtomicInteger(1);

        for (Long time : availability.times()) {
            statement.setInt(jdbcIndex.getAndIncrement(), computationId);
            statement.setString(jdbcIndex.getAndIncrement(), availability.participant());
            statement.setTimestamp(jdbcIndex.getAndIncrement(), new Timestamp(time));
            statement.setTimestamp(jdbcIndex.getAndIncrement(), new Timestamp(createdAt));
        }

        statement.execute();
    }
}
