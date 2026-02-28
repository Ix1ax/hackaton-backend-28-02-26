package dev.ixlax.backend.auth.response;

import dev.ixlax.backend.entities.UserEntity;
import dev.ixlax.backend.entities.UserRoleEnum;
import dev.ixlax.backend.security.UserPrincipal;

public record MeResponse(
        Long id,
        String email,
        String name,
        String surname,
        String patronymic,
        Integer age,
        String parentFullName,
        String parentPhone,
        UserRoleEnum role
) {
    public static MeResponse from(UserPrincipal principal) {
        return from(principal.getUser());
    }

    public static MeResponse from(UserEntity user) {
        return new MeResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getAge(),
                user.getParentFullName(),
                user.getParentPhone(),
                user.getRole()
        );
    }
}
