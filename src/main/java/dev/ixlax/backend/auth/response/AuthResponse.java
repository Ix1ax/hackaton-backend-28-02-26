package dev.ixlax.backend.auth.response;

import dev.ixlax.backend.entities.UserEntity;
import dev.ixlax.backend.entities.UserRoleEnum;

public record AuthResponse(
        String accessToken,
        Long id,
        String email,
        String name,
        String surname,
        String patronymic,
        Integer age,
        UserRoleEnum role
) {
    public static AuthResponse from(UserEntity user, String accessToken) {
        return new AuthResponse(
                accessToken,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getAge(),
                user.getRole()
        );
    }
}
