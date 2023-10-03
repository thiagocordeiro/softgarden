package de.softgarden.scheduler.infrastructure.mysql;

import de.softgarden.scheduler.domain.Meeting;
import de.softgarden.scheduler.domain.Range;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MysqlMeetingsRepositoryTest {
    private static final int COMPUTATION_ID = 876;

    private final Connection connection = mock();
    private final MysqlMeetingsRepository repository = new MysqlMeetingsRepository(connection);
    private final Meeting meeting = new Meeting(new Range(1695718800000L, 1695726000000L), List.of("Adrian", "Johanna"));

    @Test
    void givenAMeetingThenInsertIntoTheDatabase() throws SQLException {
        PreparedStatement statement = mock();
        when(connection.prepareStatement(any())).thenReturn(statement);

        repository.store(COMPUTATION_ID, meeting);

        verify(connection, times(1))
                .prepareStatement("INSERT INTO meetings (compute_id, participants, optimal_range) VALUES (?,?,?)");
        verify(statement, times(1)).setInt(1, COMPUTATION_ID);
        verify(statement, times(1)).setString(2, "Adrian, Johanna");
        verify(statement, times(1)).setString(3, "Tuesday 09:00-11:00");
    }
}
