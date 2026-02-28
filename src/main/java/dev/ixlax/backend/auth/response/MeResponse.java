package dev.ixlax.backend.auth.response;

import dev.ixlax.backend.entities.UserEntity;
import dev.ixlax.backend.security.UserPrincipal;

public record MeResponse(
        Long id,
        String email,
        String name,
        String surname,
        String patronymic
) {
    public static MeResponse from(UserPrincipal principal) {
        UserEntity user = principal.getUser();
        return new MeResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getSurname(),
                user.getPatronymic()
        );
    }
}

