package de.softgarden.scheduler.application.meetings;

import de.softgarden.scheduler.application.CommandHandler;
import de.softgarden.scheduler.application.meetings.ComputeOptimalSlotCommand.Response;
import de.softgarden.scheduler.domain.Computation;
import de.softgarden.scheduler.domain.Meeting;
import de.softgarden.scheduler.domain.OptimalMeetingComputations;
import de.softgarden.scheduler.domain.Slots;
import de.softgarden.scheduler.domain.exceptions.DomainException;
import jakarta.inject.Singleton;

@Singleton
public class ComputeOptimalSlotCommandHandler implements CommandHandler<ComputeOptimalSlotCommand, Response> {
    private final OptimalMeetingComputations computations;

    ComputeOptimalSlotCommandHandler(OptimalMeetingComputations computations) {
        this.computations = computations;
    }

    @Override
    public Response handle(ComputeOptimalSlotCommand command) throws DomainException {
        Slots slots = Slots.inlineFromAvailabilities(command.availabilities());
        Meeting meeting = Meeting.fromSlots(slots);

        computations.store(Computation.create(command.availabilities(), meeting));

        return new Response(
                meeting.range().describe(),
                meeting.participants()
        );
    }
}
