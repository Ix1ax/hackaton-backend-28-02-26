package dev.ixlax.backend.auth.request;

public record RegisterRequest(
        String email,
        String password,
        String name,
        String surname,
        String patronymic
) {
}

