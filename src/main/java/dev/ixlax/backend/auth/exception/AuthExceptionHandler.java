package dev.ixlax.backend.auth.exception;

import dev.ixlax.backend.auth.service.AuthService;
import dev.ixlax.backend.utils.MessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<MessageException> handleAuth(AuthException ex) {
        return ResponseEntity.status(401).body(ex.toMessageException());
    }
}

