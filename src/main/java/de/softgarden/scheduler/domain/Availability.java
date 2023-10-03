package de.softgarden.scheduler.domain;

import de.softgarden.scheduler.domain.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;

public record Availability(
        String participant,
        List<Long> times,
        List<String> availabilities
) {
    public static Availability fromPersonAvailabilities(String participant, List<String> availabilities) throws DomainException {
        List<Long> times = new ArrayList<>();

        for (String availability : availabilities) {
            Range range = Range.fromString(availability);

            for (long hour = range.start(); hour < range.end(); hour += Range.HOUR_IN_MILLISECONDS) {
                times.add(hour);
            }
        }

        return new Availability(participant, times, availabilities);
    }
}
