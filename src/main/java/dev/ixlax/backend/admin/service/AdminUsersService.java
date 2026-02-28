package dev.ixlax.backend.admin.service;

import dev.ixlax.backend.admin.dto.AdminUpdateUserRequest;
import dev.ixlax.backend.common.exception.BadRequestException;
import dev.ixlax.backend.common.exception.ConflictException;
import dev.ixlax.backend.common.exception.NotFoundException;
import dev.ixlax.backend.entities.UserEntity;
import dev.ixlax.backend.entities.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUsersService {

    private final UserRepository userRepository;

    public AdminUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> listUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public UserEntity getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "Пользователь не найден"));
    }

    public UserEntity updateUser(long id, AdminUpdateUserRequest request) {
        UserEntity user = getUser(id);

        if (request.email() != null) {
            String email = formatEmail(request.email());
            if (email == null || email.isBlank()) {
                throw new BadRequestException("EMAIL_REQUIRED", "Email обязателен");
            }
            if (userRepository.existsByEmailAndIdNot(email, user.getId())) {
                throw new ConflictException("EMAIL_TAKEN", "Почта уже используется");
            }
            user.setEmail(email);
        }

        if (request.name() != null) {
            user.setName(trimToNull(request.name()));
        }
        if (request.surname() != null) {
            user.setSurname(trimToNull(request.surname()));
        }
        if (request.patronymic() != null) {
            user.setPatronymic(trimToNull(request.patronymic()));
        }

        if (request.age() != null) {
            user.setAge(request.age());
        }
        if (request.parentFullName() != null) {
            user.setParentFullName(trimToNull(request.parentFullName()));
        }
        if (request.parentPhone() != null) {
            user.setParentPhone(normalizePhone(request.parentPhone()));
        }

        if (request.role() != null) {
            user.setRole(request.role());
        }

        validateParentData(user);

        return userRepository.save(user);
    }

    public UserEntity blockUser(long id) {
        UserEntity user = getUser(id);
        user.setBlocked(true);
        return userRepository.save(user);
    }

    public UserEntity unblockUser(long id) {
        UserEntity user = getUser(id);
        user.setBlocked(false);
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        UserEntity user = getUser(id);
        userRepository.delete(user);
    }

    private static void validateParentData(UserEntity user) {
        Integer age = user.getAge();
        if (age != null && age < 14) {
            if (trimToNull(user.getParentFullName()) == null || trimToNull(user.getParentPhone()) == null) {
                throw new BadRequestException(
                        "PARENT_REQUIRED",
                        "Если пользователю меньше 14 лет, нужно указать ФИО родителя и его номер телефона"
                );
            }
        }
    }

    private static String formatEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static String normalizePhone(String phone) {
        String trimmed = trimToNull(phone);
        if (trimmed == null) {
            return null;
        }
        String normalized = trimmed.replaceAll("[\\s()\\-]", "");
        if (!normalized.matches("^\\+?\\d{10,15}$")) {
            throw new BadRequestException("PHONE_INVALID", "Некорректный номер телефона родителя");
        }
        return normalized;
    }
}

