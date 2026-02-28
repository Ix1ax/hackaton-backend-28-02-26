package dev.ixlax.backend.admin.dto;

import dev.ixlax.backend.entities.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Email;

public record AdminUpdateUserRequest(
        @Schema(nullable = true, example = "user@example.com")
        @Email(message = "Некорректный email")
        String email,

        @Schema(nullable = true, example = "Иван")
        String name,

        @Schema(nullable = true, example = "Иванов")
        String surname,

        @Schema(nullable = true, example = "Иванович")
        String patronymic,

        @Schema(nullable = true, example = "16")
        @Min(value = 0, message = "Возраст должен быть >= 0")
        @Max(value = 150, message = "Возраст должен быть <= 150")
        Integer age,

        @Schema(nullable = true, example = "Петров Пётр Петрович")
        String parentFullName,

        @Schema(nullable = true, example = "+79991234567")
        String parentPhone,

        @Schema(nullable = true, example = "USER")
        UserRoleEnum role
) {
}
