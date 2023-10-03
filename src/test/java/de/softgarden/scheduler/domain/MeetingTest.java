package de.softgarden.scheduler.domain;

import de.softgarden.scheduler.domain.exceptions.BusinessRuleException;
import de.softgarden.scheduler.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MeetingTest {
    @Test
    void givenAListOfSlotsAndItsParticipantsWhenThereIsNoMatchingSlotThenThrowBusinessRuleException() {
        Slots slots = new Slots(
                new TreeMap<>(
                        Map.of(
                                1695654000000L, new HashSet<>(List.of("Adrian")),
                                1695722400000L, new HashSet<>(List.of("Sebastian")),
                                1695726000000L, new HashSet<>(List.of("Johanna"))
                        )
                ),
                3
        );

        BusinessRuleException exception = Assertions.assertThrows(
                BusinessRuleException.class,
                () -> Meeting.fromSlots(slots)
        );

        Assertions.assertEquals("No matching slots available for the given participants", exception.getMessage());
    }

    @Test
    void givenAListOfSlotsAndItsParticipantsThenReturnPossibleMeetings() throws DomainException {
        Slots slots = new Slots(
                new TreeMap<>(
                        Map.of(
                                1695654000000L, new HashSet<>(List.of("Adrian")),
                                1695722400000L, new HashSet<>(List.of("Adrian", "Johanna", "Sebastian")),
                                1695726000000L, new HashSet<>(List.of("Johanna"))
                        )
                ),
                3
        );

        Meeting meetings = Meeting.fromSlots(slots);

        Assertions.assertEquals(
                new Meeting(
                        new Range(1695722400000L, 1695726000000L),
                        List.of("Adrian", "Johanna", "Sebastian")
                ),
                meetings
        );
    }
}
