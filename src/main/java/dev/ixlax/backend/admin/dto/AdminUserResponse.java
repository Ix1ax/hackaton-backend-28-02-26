package dev.ixlax.backend.admin.dto;

import dev.ixlax.backend.entities.UserEntity;
import dev.ixlax.backend.entities.UserRoleEnum;

public record AdminUserResponse(
        Long id,
        String email,
        String name,
        String surname,
        String patronymic,
        Integer age,
        String parentFullName,
        String parentPhone,
        UserRoleEnum role,
        boolean blocked
) {
    public static AdminUserResponse from(UserEntity user) {
        return new AdminUserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getAge(),
                user.getParentFullName(),
                user.getParentPhone(),
                user.getRole(),
                user.isBlocked()
        );
    }
}
