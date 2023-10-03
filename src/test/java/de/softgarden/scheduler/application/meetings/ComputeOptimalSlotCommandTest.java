package de.softgarden.scheduler.application.meetings;

import de.softgarden.scheduler.application.meetings.ComputeOptimalSlotCommand;
import de.softgarden.scheduler.domain.Availability;
import de.softgarden.scheduler.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ComputeOptimalSlotCommandTest {
    @Test
    void givenAListOfParticipantsAndTheirAvailabilityRangesThenCreateTheCommandWithParticipantsTimes() throws DomainException {
        TreeMap<String, List<String>> availabilities = new TreeMap<>(
                Map.of(
                        "Adrian", List.of("Monday 14:00-16:00", "Tuesday 09:00-11:00"),
                        "Johanna", List.of("Monday 15:00-17:00", "Wednesday 10:00-12:00"),
                        "Sebastian", List.of("Tuesday 09:00-11:00", "Wednesday 11:00-13:00")
                )
        );

        ComputeOptimalSlotCommand command = ComputeOptimalSlotCommand.fromJoinersAvailabilities(availabilities);

        Assertions.assertEquals(
                List.of(
                        new Availability(
                                "Adrian",
                                List.of(1695650400000L, 1695654000000L, 1695718800000L, 1695722400000L),
                                List.of("Monday 14:00-16:00", "Tuesday 09:00-11:00")
                        ),
                        new Availability(
                                "Johanna",
                                List.of(1695654000000L, 1695657600000L, 1695808800000L, 1695812400000L),
                                List.of("Monday 15:00-17:00", "Wednesday 10:00-12:00")
                        ),
                        new Availability(
                                "Sebastian",
                                List.of(1695718800000L, 1695722400000L, 1695812400000L, 1695816000000L),
                                List.of("Tuesday 09:00-11:00", "Wednesday 11:00-13:00")
                        )
                ),
                command.availabilities()
        );
    }
}
