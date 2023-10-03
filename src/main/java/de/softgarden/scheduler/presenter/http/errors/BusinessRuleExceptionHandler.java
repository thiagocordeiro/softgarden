package de.softgarden.scheduler.presenter.http.errors;

import de.softgarden.scheduler.domain.exceptions.BusinessRuleException;
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
@Requires(classes = {BusinessRuleException.class, ExceptionHandler.class})
public class BusinessRuleExceptionHandler implements ExceptionHandler<BusinessRuleException, HttpResponse<?>> {
    @Override
    public HttpResponse<Map<String, String>> handle(HttpRequest request, BusinessRuleException exception) {
        return HttpResponse
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("message", exception.getMessage()));
    }
}
