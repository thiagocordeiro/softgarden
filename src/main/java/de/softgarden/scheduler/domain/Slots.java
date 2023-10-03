package de.softgarden.scheduler.domain;

import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public record Slots(
        TreeMap<Long, HashSet<String>> times,
        int maxParticipants
) {
    private static final int MINIMUM_JOINERS = 2;

    public static Slots inlineFromAvailabilities(List<Availability> availabilities) {
        TreeMap<Long, HashSet<String>> times = new TreeMap<>();
        AtomicInteger maxParticipants = new AtomicInteger(MINIMUM_JOINERS);

        availabilities.forEach((availability) -> {
            availability.times().forEach(range -> availability.times().forEach(time -> {
                HashSet<String> participants = times.getOrDefault(time, new HashSet<>());
                participants.add(availability.participant());
                times.put(time, participants);

                if (participants.size() > maxParticipants.get()) maxParticipants.set(participants.size());
            }));
        });

        return new Slots(times, maxParticipants.get());
    }
}
