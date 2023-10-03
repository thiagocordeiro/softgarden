package de.softgarden.scheduler.domain;

import de.softgarden.scheduler.domain.exceptions.DomainException;
import de.softgarden.scheduler.domain.exceptions.InvalidInputDataException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public record Range(Long start, Long end) {
    public static final int HOUR_IN_MILLISECONDS = 3600000;

    public static Range fromString(String range) throws DomainException {
        String[] pieces = range.replace(" ", "-").split("-");
        String day = pieces[0]
                .replace("Monday", "2023-09-25")
                .replace("Tuesday", "2023-09-26")
                .replace("Wednesday", "2023-09-27")
                .replace("Thursday", "2023-09-28")
                .replace("Friday", "2023-09-29");

        try {
            long start = getTimeFromString(day + pieces[1]);
            long end = getTimeFromString(day + pieces[2]);

            return new Range(start, end);
        } catch (ParseException Exception) {
            throw new InvalidInputDataException("Range '" + range + "' is invalid");
        }
    }

    public static List<Range> fromStartingTimes(List<Long> times) {
        times = new ArrayList<>(times);
        Collections.sort(times);
        ListIterator<Long> iterator = times.listIterator();
        List<Range> ranges = new ArrayList<>();
        long start = iterator.next();
        long end = start + HOUR_IN_MILLISECONDS;

        while (iterator.hasNext()) {
            while (iterator.hasNext()) {
                long next = iterator.next();

                if (next != end) {
                    ranges.add(new Range(start, end));
                    start = next;
                    end = next + HOUR_IN_MILLISECONDS;
                    break;
                }

                end = next + HOUR_IN_MILLISECONDS;
            }
        }

        ranges.add(new Range(start, end));

        return ranges;
    }

    public String describe() {
        String startDate = formatter("EEEE HH:mm").format(new Date(start));
        String endDate = formatter("HH:mm").format(new Date(end));

        return startDate + "-" + endDate;
    }

    private static long getTimeFromString(String datetime) throws ParseException {
        return formatter("yyyy-MM-ddHH:mm").parse(datetime).getTime();
    }

    private static SimpleDateFormat formatter(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        formatter.setLenient(false);

        return formatter;
    }
}
