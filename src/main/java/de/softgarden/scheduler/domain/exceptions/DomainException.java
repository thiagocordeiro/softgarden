package de.softgarden.scheduler.domain.exceptions;

public abstract class DomainException extends Exception {
    DomainException(String message) {
        super(message);
    }
}
