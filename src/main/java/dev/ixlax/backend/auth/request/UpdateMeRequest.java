package dev.ixlax.backend.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

@Schema(description = "Обновление профиля. Email и дата рождения/возраст не редактируются.")
public record UpdateMeRequest(
        @Schema(nullable = true, example = "Иван")
        @Size(min = 1, max = 100, message = "Имя должно быть от 1 до 100 символов")
        String name,

        @Schema(nullable = true, example = "Иванов")
        @Size(min = 1, max = 100, message = "Фамилия должна быть от 1 до 100 символов")
        String surname,

        @Schema(nullable = true, example = "Иванович")
        @Size(max = 100, message = "Отчество должно быть до 100 символов")
        String patronymic,

        @Schema(description = "ФИО родителя (нужно, если возраст < 14)", nullable = true, example = "Петров Пётр Петрович")
        @Size(max = 200, message = "ФИО родителя должно быть до 200 символов")
        String parentFullName,

        @Schema(description = "Телефон родителя (нужно, если возраст < 14)", nullable = true, example = "+79991234567")
        @Size(max = 32, message = "Телефон родителя должен быть до 32 символов")
        String parentPhone,

        @Schema(hidden = true)
        @Null(message = "Почту менять нельзя")
        String email,

        @Schema(hidden = true)
        @Null(message = "Дату рождения/возраст менять нельзя")
        Integer age
) {
}
