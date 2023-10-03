package de.softgarden.scheduler.domain;

import de.softgarden.scheduler.domain.exceptions.BusinessRuleException;
import de.softgarden.scheduler.domain.exceptions.DomainException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record Meeting(
        Range range,
        List<String> participants
) {
    public static Meeting fromSlots(Slots slots) throws DomainException {
        TreeMap<String, HashSet<Long>> possibleMeetingRanges = new TreeMap<>();
        AtomicInteger maxPossibleTimes = new AtomicInteger();
        AtomicReference<String> optimalParticipants = new AtomicReference<>();

        slots.times().forEach((time, participants) -> {
            // exclude slots less than the know maximum participants
            if (participants.size() < slots.maxParticipants()) return;

            String key = String.join("-", participants);
            HashSet<Long> range = possibleMeetingRanges.getOrDefault(key, new HashSet<>());
            range.add(time);
            possibleMeetingRanges.put(key, range);

            // keep and state of the optimal participants
            if (range.size() > maxPossibleTimes.get()) {
                maxPossibleTimes.set(range.size());
                optimalParticipants.set(key);
            }
        });

        String participants = optimalParticipants.get();

        if (participants == null) {
            throw new BusinessRuleException("No matching slots available for the given participants");
        }

        List<Range> ranges = Range.fromStartingTimes(
                possibleMeetingRanges
                        .get(participants)
                        .stream()
                        .toList()
        );

        return new Meeting(
                ranges.get(0),
                Arrays.asList(optimalParticipants.get().split("-"))
        );
    }
}
