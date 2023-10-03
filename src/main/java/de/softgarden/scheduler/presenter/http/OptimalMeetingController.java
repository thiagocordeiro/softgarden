package de.softgarden.scheduler.presenter.http;

import de.softgarden.scheduler.application.meetings.ComputeOptimalSlotCommand;
import de.softgarden.scheduler.application.meetings.ComputeOptimalSlotCommandHandler;
import de.softgarden.scheduler.domain.exceptions.DomainException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@Controller("/meetings/slots/optimal")
public class OptimalMeetingController {
    private final ComputeOptimalSlotCommandHandler handler;

    @Inject
    OptimalMeetingController(ComputeOptimalSlotCommandHandler handler) {
        this.handler = handler;
    }

    @Post(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<?> index(@Body Map<String, List<String>> availabilities) throws DomainException {
        ComputeOptimalSlotCommand command = ComputeOptimalSlotCommand.fromJoinersAvailabilities(availabilities);

        ComputeOptimalSlotCommand.Response response = handler.handle(command);

        return HttpResponse
                .status(HttpStatus.OK)
                .body(response);
    }
}
