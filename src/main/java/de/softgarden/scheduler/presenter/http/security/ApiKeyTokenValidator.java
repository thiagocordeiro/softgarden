package de.softgarden.scheduler.presenter.http.security;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.Objects;

@Singleton
class ApiKeyTokenValidator implements TokenValidator<HttpRequest<?>> {
    @Value("${softgarden.api.key}")
    private String apiKey;

    @Override
    public Publisher<Authentication> validateToken(String token, HttpRequest<?> request) {
        if (!Objects.equals(token, apiKey)) {
            return Publishers.empty();
        }

        return Publishers.just(Authentication.build("Api Key"));
    }
}
