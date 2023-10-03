package de.softgarden.scheduler.application.meetings;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.softgarden.scheduler.application.Command;
import de.softgarden.scheduler.domain.Availability;
import de.softgarden.scheduler.domain.exceptions.DomainException;
import io.micronaut.serde.annotation.Serdeable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ComputeOptimalSlotCommand(
        List<Availability> availabilities
) implements Command<ComputeOptimalSlotCommand.Response> {
    @Serdeable.Serializable
    public record Response(
            @JsonProperty("Optimal Slot")
            String optimalSlot,
            @JsonProperty("Participants")
            List<String> participants
    ) {
    }

    public static ComputeOptimalSlotCommand fromJoinersAvailabilities(Map<String, List<String>> availabilities) throws DomainException {
        List<Availability> mapped = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : availabilities.entrySet()) {
            mapped.add(Availability.fromPersonAvailabilities(entry.getKey(), entry.getValue()));
        }

        return new ComputeOptimalSlotCommand(mapped);
    }
}
