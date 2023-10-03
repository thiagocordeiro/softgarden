package de.softgarden.scheduler.application.meetings;

import de.softgarden.scheduler.application.meetings.ComputeOptimalSlotCommand.Response;
import de.softgarden.scheduler.domain.Computation;
import de.softgarden.scheduler.domain.exceptions.DomainException;
import de.softgarden.scheduler.infrastructure.InMemoryOptimalMeetingComputations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class ComputeOptimalSlotCommandHandlerTest {
    private final InMemoryOptimalMeetingComputations repository = new InMemoryOptimalMeetingComputations();
    private final ComputeOptimalSlotCommandHandler handler = new ComputeOptimalSlotCommandHandler(repository);

    @Test
    void givenAListParticipantsAndItsAvailabilitiesWhenTwoOfThemHaveCommonSlotsThenReturnTheSlotRange() throws DomainException {
        ComputeOptimalSlotCommand command = ComputeOptimalSlotCommand.fromJoinersAvailabilities(Map.ofEntries(
                entry("Adrian", List.of("Monday 14:00-16:00", "Tuesday 09:00-11:00")),
                entry("Johanna", List.of("Monday 15:00-17:00", "Wednesday 10:00-12:00")),
                entry("Sebastian", List.of("Tuesday 09:00-11:00", "Wednesday 11:00-13:00"))
        ));

        Response response = handler.handle(command);

        Assertions.assertEquals(new Response("Tuesday 09:00-11:00", List.of("Adrian", "Sebastian")), response);
        Computation stored = repository.stored().get(0);
        Assertions.assertEquals(1, stored.id());
        Assertions.assertEquals("Tuesday 09:00-11:00", stored.meeting().range().describe());
        Assertions.assertEquals(List.of("Adrian", "Sebastian"), stored.meeting().participants());
    }

    @Test
    void givenAListParticipantsAndItsAvailabilitiesWhenThreeOfThemHaveCommonSlotsThenReturnTheSlotRange() throws DomainException {
        ComputeOptimalSlotCommand command = ComputeOptimalSlotCommand.fromJoinersAvailabilities(Map.ofEntries(
                entry("Adrian", List.of("Monday 14:00-16:00", "Tuesday 09:00-11:00")),
                entry("Johanna", List.of("Monday 15:00-17:00", "Tuesday 10:00-12:00", "Wednesday 10:00-12:00")),
                entry("Sebastian", List.of("Tuesday 09:00-11:00", "Wednesday 11:00-13:00"))
        ));

        Response response = handler.handle(command);

        Computation stored = repository.stored().get(0);
        Assertions.assertEquals(1, stored.id());
        Assertions.assertEquals("Tuesday 10:00-11:00", stored.meeting().range().describe());
        Assertions.assertEquals(List.of("Adrian", "Johanna", "Sebastian"), stored.meeting().participants());
        Assertions.assertEquals(new Response("Tuesday 10:00-11:00", List.of("Adrian", "Johanna", "Sebastian")), response);
    }
}
