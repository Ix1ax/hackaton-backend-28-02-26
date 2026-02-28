package dev.ixlax.backend.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "user@example.com")
        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный email")
        String email,

        @Schema(example = "password123")
        @NotBlank(message = "Пароль обязателен")
        String password
) {
}
