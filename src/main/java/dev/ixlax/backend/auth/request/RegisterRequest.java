package dev.ixlax.backend.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Schema(example = "user@example.com")
        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный email")
        String email,

        @Schema(example = "password123")
        @NotBlank(message = "Пароль обязателен")
        @Size(min = 6, max = 72, message = "Пароль должен быть от 6 до 72 символов")
        String password,

        @Schema(example = "Иван")
        @NotBlank(message = "Имя обязательно")
        String name,

        @Schema(example = "Иванов")
        @NotBlank(message = "Фамилия обязательна")
        String surname,

        @Schema(example = "Иванович", nullable = true)
        String patronymic,

        @Schema(description = "Возраст пользователя. Если меньше 14 — обязательны данные родителя.", example = "16")
        @NotNull(message = "Возраст обязателен")
        @Min(value = 0, message = "Возраст должен быть >= 0")
        @Max(value = 150, message = "Возраст должен быть <= 150")
        Integer age,

        @Schema(description = "ФИО родителя (обязательно, если возраст < 14)", example = "Петров Пётр Петрович", nullable = true)
        String parentFullName,

        @Schema(description = "Телефон родителя (обязательно, если возраст < 14)", example = "+79991234567", nullable = true)
        String parentPhone
) {
}
