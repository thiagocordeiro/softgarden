package de.softgarden.scheduler.domain;

import java.util.Date;
import java.util.List;

public record Computation(
        int id,
        List<Availability> availabilities,
        Meeting meeting,
        Date createdAt
) {
    public static Computation create(List<Availability> availabilities, Meeting meeting) {
        return new Computation(
                0,
                availabilities,
                meeting,
                new Date()
        );
    }
}
