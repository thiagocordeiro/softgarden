package de.softgarden.scheduler.domain;

import de.softgarden.scheduler.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AvailabilityTest {
    @Test
    void givenAParticipantAndItsAvailabilityRangesThenConvertIntoAvailabilityTimes() throws DomainException {
        String participant = "Adrian";
        List<String> availabilities = List.of("Monday 14:00-16:00", "Tuesday 09:00-12:00");

        Availability availability = Availability.fromPersonAvailabilities(participant, availabilities);

        Assertions.assertEquals(
                new Availability(
                        "Adrian",
                        List.of(
                                1695650400000L, // Monday 14:00
                                1695654000000L, // Monday 15:00
                                1695718800000L, // Tuesday 09:00
                                1695722400000L, // Tuesday 10:00
                                1695726000000L  // Tuesday 11:00
                        ),
                        List.of("Monday 14:00-16:00", "Tuesday 09:00-12:00")
                ),
                availability
        );
    }
}
