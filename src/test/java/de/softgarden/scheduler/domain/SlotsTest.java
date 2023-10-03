package de.softgarden.scheduler.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SlotsTest {
    @Test
    void givenAListOfParticipantsAndTheirAvailabilitiesThenReturnPossibleMeetingSlotsAndItsParticipants() {
        List<Availability> availabilities = List.of(
                new Availability(
                        "Adrian",
                        List.of(
                                1695650400000L, // Monday 14:00
                                1695654000000L, // Monday 15:00
                                1695722400000L // Tuesday 10:00
                        ),
                        List.of("Monday 14:00-16:00", "Tuesday 10:00-11:00")
                ),
                new Availability(
                        "Johanna",
                        List.of(
                                1695718800000L, // Tuesday 09:00
                                1695722400000L, // Tuesday 10:00
                                1695726000000L  // Tuesday 11:00
                        ),
                        List.of("Tuesday 10:00-12:00")
                ),
                new Availability(
                        "Sebastian",
                        List.of(
                                1695722400000L // Tuesday 10:00
                        ),
                        List.of("Tuesday 10:00-11:00")
                )
        );

        Slots slots = Slots.inlineFromAvailabilities(availabilities);

        Assertions.assertEquals(
                new Slots(
                        new TreeMap<>(
                                Map.of(
                                        1695650400000L, new HashSet<>(List.of("Adrian")),
                                        1695654000000L, new HashSet<>(List.of("Adrian")),
                                        1695718800000L, new HashSet<>(List.of("Johanna")),
                                        1695722400000L, new HashSet<>(List.of("Adrian", "Johanna", "Sebastian")),
                                        1695726000000L, new HashSet<>(List.of("Johanna"))
                                )
                        ),
                        3
                ),
                slots
        );
    }
}
