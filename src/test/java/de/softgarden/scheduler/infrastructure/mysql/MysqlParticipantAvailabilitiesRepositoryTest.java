package de.softgarden.scheduler.infrastructure.mysql;

import de.softgarden.scheduler.domain.Availability;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.mockito.Mockito.*;

public class MysqlParticipantAvailabilitiesRepositoryTest {
    private static final int COMPUTATION_ID = 876;

    private final Connection connection = mock();
    private final MysqlParticipantAvailabilitiesRepository repository = new MysqlParticipantAvailabilitiesRepository(connection);
    private final Availability availability = new Availability(
            "Adrian",
            List.of(
                    1695650400000L, // Monday 14:00
                    1695654000000L, // Monday 15:00
                    1695722400000L // Tuesday 10:00
            ),
            List.of("Monday 14:00-16:00", "Tuesday 10:00-11:00")
    );

    @Test
    void givenAnAvailabilityThenStore() throws SQLException {
        PreparedStatement statement = mock();
        when(connection.prepareStatement(any())).thenReturn(statement);

        repository.storeParticipantAvailability(COMPUTATION_ID, availability);

        verify(connection, times(1)).prepareStatement(
                "INSERT INTO participant_availabilities " +
                        "(compute_id, participant, available_at, created_at) " +
                        "VALUES (?,?,?,?),(?,?,?,?),(?,?,?,?)");

        verify(statement, times(1)).setInt(1, COMPUTATION_ID);
        verify(statement, times(1)).setString(2, "Adrian");
        verify(statement, times(1)).setTimestamp(3, new Timestamp(1695650400000L));

        verify(statement, times(1)).setInt(5, COMPUTATION_ID);
        verify(statement, times(1)).setString(6, "Adrian");
        verify(statement, times(1)).setTimestamp(7, new Timestamp(1695654000000L));

        verify(statement, times(1)).setInt(9, COMPUTATION_ID);
        verify(statement, times(1)).setString(10, "Adrian");
        verify(statement, times(1)).setTimestamp(11, new Timestamp(1695722400000L));
    }
}
