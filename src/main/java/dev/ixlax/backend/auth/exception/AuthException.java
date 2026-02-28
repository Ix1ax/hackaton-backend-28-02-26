package dev.ixlax.backend.auth.exception;

import dev.ixlax.backend.utils.MessageException;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }

    public MessageException toMessageException() {
        return new MessageException("401", getMessage());
    }
}
