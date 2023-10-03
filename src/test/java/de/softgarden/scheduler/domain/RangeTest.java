package de.softgarden.scheduler.domain;

import de.softgarden.scheduler.domain.exceptions.InvalidInputDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class RangeTest {
    @Test
    void givenARangeWhenTheHourRangeIsInvalidThenThrowInvalidInputDataException() {
        String range = "Tuesday 20:00-26:00";

        InvalidInputDataException exception = Assertions.assertThrows(
                InvalidInputDataException.class,
                () -> Range.fromString(range)
        );

        Assertions.assertEquals("Range 'Tuesday 20:00-26:00' is invalid", exception.getMessage());
    }

    @Test
    void givenAListOfStartingTimesThenReturnAListWithOneRangeFromTheFirstTimeUntilLastTimePlusOneHour() {
        List<Long> times = Arrays.asList(
                1695718800000L, // Tuesday 09:00
                1695722400000L // Tuesday 10:00
        );

        List<Range> ranges = Range.fromStartingTimes(times);

        Assertions.assertEquals(
                List.of(
                        new Range(1695718800000L, 1695726000000L) // Tuesday 09:00-11:00
                ),
                ranges
        );
    }

    @Test
    void givenAListOfStartingTimesThenReturnAListWithManyRangeFromTheFirstTimeUntilLastTimePlusOneHour() {
        List<Long> times = Arrays.asList(
                1695650400000L, // Monday 14:00
                1695654000000L, // Monday 15:00
                1695812400000L, // Wednesday 11:00
                1695816000000L, // Wednesday 12:00
                1695819600000L, // Wednesday 13:00
                1695823200000L  // Wednesday 14:00
        );

        List<Range> ranges = Range.fromStartingTimes(times);

        Assertions.assertEquals(
                List.of(
                        new Range(1695650400000L, 1695657600000L), // Monday 14:00-16:00
                        new Range(1695812400000L, 1695826800000L) // Wednesday 11:00-15:00
                ),
                ranges
        );
    }

    @Test
    void givenARangeThenGetItsDescription() {
        Assertions.assertEquals("Tuesday 09:00-11:00", new Range(1695718800000L, 1695726000000L).describe());
        Assertions.assertEquals("Monday 14:00-16:00", new Range(1695650400000L, 1695657600000L).describe());
        Assertions.assertEquals("Wednesday 11:00-15:00", new Range(1695812400000L, 1695826800000L).describe());
    }
}
