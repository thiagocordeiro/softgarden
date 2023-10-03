package de.softgarden.scheduler.presenter.http;

import de.softgarden.scheduler.domain.OptimalMeetingComputations;
import de.softgarden.scheduler.infrastructure.InMemoryOptimalMeetingComputations;
import io.micronaut.context.annotation.Factory;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class OptimalMeetingEndpointTest {
    private static final String API_KEY = "testing-softgarden-scheduler-api-key";

    private final InMemoryOptimalMeetingComputations repository = new InMemoryOptimalMeetingComputations();

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testGivenTheParticipantsAvailabilityRangesThenComputeTheOptimalMeetingSlot() {
        HttpRequest<String> request = HttpRequest.POST(
                "/meetings/slots/optimal",
                """
                    {
                        "Adrian": ["Monday 14:00-16:00", "Tuesday 09:00-11:00"],
                        "Johanna": ["Monday 15:00-17:00", "Wednesday 10:00-12:00"],
                        "Sebastian": ["Tuesday 09:00-11:00", "Wednesday 11:00-13:00"]
                    }
                """.trim()
        ).header("X-API-KEY", API_KEY);

        HttpResponse<String> response = client.toBlocking().exchange(request);

        Assertions.assertEquals(HttpStatus.OK, response.status());
        Assertions.assertEquals(
                """
                    {"Optimal Slot":"Tuesday 09:00-11:00","Participants":["Adrian","Sebastian"]}
                """.trim(),
                response.getBody(String.class).get()
        );
    }

    @Test
    void testGivenTheParticipantsAvailabilityRangesWhenNoMatchingSlotsAreFoundThenReturnUnprocessableEntity() {
        HttpRequest<String> request = HttpRequest.POST(
                "/meetings/slots/optimal",
                """
                    {
                        "Adrian": ["Monday 14:00-16:00"],
                        "Johanna": ["Tuesday 09:00-11:00"],
                        "Sebastian": ["Wednesday 11:00-13:00"]
                    }
                """.trim()
        ).header("X-API-KEY", API_KEY);

        HttpClientResponseException exception = Assertions.assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(request)
        );

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
        Assertions.assertEquals(
                """
                    {"message":"No matching slots available for the given participants"}
                """.trim(),
                exception.getResponse().getBody(String.class).get()
        );
    }

    @Test
    void testGivenInvalidDateRangesThenReturnBadRequest() {
        HttpRequest<String> request = HttpRequest.POST(
                "/meetings/slots/optimal",
                """
                    {
                        "Adrian": ["Monday 14:00-27:00"]
                    }
                """.trim()
        ).header("X-API-KEY", API_KEY);

        HttpClientResponseException exception = Assertions.assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(request)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(
                """
                    {"message":"Range 'Monday 14:00-27:00' is invalid"}
                """.trim(),
                exception.getResponse().getBody(String.class).get()
        );
    }

    @Test
    void testGivenAPayloadWhenItIsInvalidThenThrowBadRequest() {
        HttpRequest<String> request = HttpRequest.POST(
                "/meetings/slots/optimal",
                """
                    {
                        "Adrian": {
                            "day": "Monday 14:00-27:00"
                        }
                    }
                """.trim()
        ).header("X-API-KEY", API_KEY);

        HttpClientResponseException exception = Assertions.assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(request)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals("Bad Request", exception.getMessage());
    }

    @MockBean(OptimalMeetingComputations.class)
    public OptimalMeetingComputations dependency() {
        return repository;
    }
}
