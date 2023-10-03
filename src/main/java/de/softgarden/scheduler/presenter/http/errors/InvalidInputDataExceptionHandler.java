package de.softgarden.scheduler.presenter.http.errors;

import de.softgarden.scheduler.domain.exceptions.InvalidInputDataException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import java.util.Map;

@Produces
@Singleton
@Requires(classes = {InvalidInputDataException.class, ExceptionHandler.class})
public class InvalidInputDataExceptionHandler implements ExceptionHandler<InvalidInputDataException, HttpResponse<?>> {
    @Override
    public HttpResponse<Map<String, String>> handle(HttpRequest request, InvalidInputDataException exception) {
        return HttpResponse
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", exception.getMessage()));
    }
}
