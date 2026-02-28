package dev.ixlax.backend.common.exception;

import dev.ixlax.backend.auth.exception.AuthException;
import dev.ixlax.backend.common.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<MessageResponse> handleAuth(AuthException ex) {
        return ResponseEntity.status(401).body(new MessageResponse("401", ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<MessageResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404).body(new MessageResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<MessageResponse> handleConflict(ConflictException ex) {
        return ResponseEntity.status(409).body(new MessageResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleBeanValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getDefaultMessage() == null ? "Некорректное поле: " + err.getField() : err.getDefaultMessage())
                .orElse("Некорректные данные");

        return ResponseEntity.badRequest().body(new MessageResponse("VALIDATION_ERROR", message));
    }
}
