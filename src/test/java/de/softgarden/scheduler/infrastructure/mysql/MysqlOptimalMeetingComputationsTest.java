package de.softgarden.scheduler.infrastructure.mysql;

import de.softgarden.scheduler.domain.Availability;
import de.softgarden.scheduler.domain.Computation;
import de.softgarden.scheduler.domain.Meeting;
import de.softgarden.scheduler.domain.Range;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class MysqlOptimalMeetingComputationsTest {
    private final Connection connection = mock();
    private final MysqlParticipantAvailabilitiesRepository availabilityRepository = mock();
    private final MysqlMeetingsRepository meetingsRepository = mock();

    private final MysqlOptimalMeetingComputations repository = new MysqlOptimalMeetingComputations(
            connection,
            availabilityRepository,
            meetingsRepository
    );

    private final Availability availability = new Availability(
            "Adrian",
            List.of(1695650400000L, 1695654000000L, 1695722400000L),
            List.of("Monday 14:00-16:00", "Tuesday 10:00-11:00")
    );
    private final Meeting meeting = new Meeting(
            new Range(1695718800000L, 1695726000000L),
            List.of("Adrian", "Johanna")
    );

    @Test
    void givenAComputationThenStoreInTheDatabase() throws SQLException {
        PreparedStatement statement = mock();
        ResultSet resultSet = mock();
        when(connection.prepareStatement(
                "INSERT INTO optimal_meeting_computations (created_at) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
        )).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(resultSet);
        Date now = new Date();
        Computation computation = new Computation(1, List.of(availability), meeting, now);

        repository.store(computation);

        verify(connection, times(1)).prepareStatement(
                "INSERT INTO optimal_meeting_computations (created_at) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
        );
        verify(statement, times(1)).getGeneratedKeys();
        verify(statement, times(1)).setTimestamp(1, new Timestamp(now.getTime()));
    }
}
