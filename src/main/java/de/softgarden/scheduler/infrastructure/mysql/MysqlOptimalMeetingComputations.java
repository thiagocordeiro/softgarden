package de.softgarden.scheduler.infrastructure.mysql;

import de.softgarden.scheduler.domain.Availability;
import de.softgarden.scheduler.domain.Computation;
import de.softgarden.scheduler.domain.OptimalMeetingComputations;
import jakarta.inject.Singleton;

import java.sql.*;

@Singleton
class MysqlOptimalMeetingComputations implements OptimalMeetingComputations {
    private final Connection connection;
    private final MysqlParticipantAvailabilitiesRepository availabilityRepository;
    private final MysqlMeetingsRepository meetingsRepository;

    MysqlOptimalMeetingComputations(
            Connection connection,
            MysqlParticipantAvailabilitiesRepository availabilityRepository,
            MysqlMeetingsRepository meetingsRepository
    ) {
        this.connection = connection;
        this.availabilityRepository = availabilityRepository;
        this.meetingsRepository = meetingsRepository;
    }

    @Override
    public void store(Computation computation) {
        try {
            int computationId = storeComputation(computation);

            for (Availability availability : computation.availabilities()) {
                availabilityRepository.storeParticipantAvailability(computationId, availability);
            }
            meetingsRepository.store(computationId, computation.meeting());
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to store availability", exception);
        }
    }

    private int storeComputation(Computation computation) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO optimal_meeting_computations (created_at) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setTimestamp(1, new Timestamp(computation.createdAt().getTime()));
        statement.execute();

        ResultSet keys = statement.getGeneratedKeys();
        keys.next();

        return keys.getInt(1);
    }
}
