package de.softgarden.scheduler.infrastructure;

import de.softgarden.scheduler.domain.Computation;
import de.softgarden.scheduler.domain.OptimalMeetingComputations;

import java.util.ArrayList;
import java.util.List;

public class InMemoryOptimalMeetingComputations implements OptimalMeetingComputations {
    private int index = 1;
    private final List<Computation> dataset = new ArrayList<>();

    @Override
    public void store(Computation compute) {
        Computation stored = new Computation(
                index++,
                compute.availabilities(),
                compute.meeting(),
                compute.createdAt()
        );

        this.dataset.add(stored);
    }

    public List<Computation> stored() {
        return dataset;
    }
}
