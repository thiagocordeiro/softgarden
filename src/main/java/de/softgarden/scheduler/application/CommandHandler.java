package de.softgarden.scheduler.application;

import de.softgarden.scheduler.domain.exceptions.DomainException;

public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command) throws DomainException;
}
