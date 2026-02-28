package dev.ixlax.backend.auth.service;

import dev.ixlax.backend.auth.exception.AuthException;
import dev.ixlax.backend.auth.request.LoginRequest;
import dev.ixlax.backend.auth.request.RegisterRequest;
import dev.ixlax.backend.auth.request.UpdateMeRequest;
import dev.ixlax.backend.auth.response.AuthResponse;
import dev.ixlax.backend.auth.response.MeResponse;
import dev.ixlax.backend.entities.UserEntity;
import dev.ixlax.backend.entities.UserRepository;
import dev.ixlax.backend.entities.UserRoleEnum;
import dev.ixlax.backend.security.JwtService;
import dev.ixlax.backend.common.exception.BadRequestException;
import dev.ixlax.backend.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = formatEmail(request.email());
        Integer age = request.age();
        if (age == null) {
            throw new BadRequestException("AGE_REQUIRED", "Возраст обязателен");
        }
        log.info("Попытка регистрации: email={}, age={}", email, age);

        String parentFullName = trimToNull(request.parentFullName());
        String parentPhone = normalizePhone(request.parentPhone());
        if (age < 14) {
            if (parentFullName == null || parentPhone == null) {
                throw new BadRequestException(
                        "PARENT_REQUIRED",
                        "Если пользователю меньше 14 лет, нужно указать ФИО родителя и его номер телефона"
                );
            }
        }

        if (userRepository.existsByEmail(email)) {
            log.warn("Регистрация отклонена: email уже занят (email={})", email);
            throw new AuthException("Почта уже использует");
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setPatronymic(request.patronymic());
        user.setAge(age);
        user.setParentFullName(parentFullName);
        user.setParentPhone(parentPhone);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(UserRoleEnum.USER);

        UserEntity saved = userRepository.save(user);
        log.info("Регистрация успешна: userId={}, email={}", saved.getId(), saved.getEmail());
        String token = jwtService.generateToken(saved.getEmail(), Map.of("uid", saved.getId()));
        return AuthResponse.from(saved, token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = formatEmail(request.email());
        log.info("Попытка входа: email={}", email);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Неправильная почта или пароль"));

        if (user.isBlocked()) {
            log.warn("Вход отклонён: пользователь заблокирован (userId={}, email={})", user.getId(), user.getEmail());
            throw new AuthException("Пользователь заблокирован");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.warn("Вход отклонён: неверные учётные данные (email={})", email);
            throw new AuthException("Неправильная почта или пароль");
        }

        log.info("Вход успешен: userId={}, email={}", user.getId(), user.getEmail());
        String token = jwtService.generateToken(user.getEmail(), Map.of("uid", user.getId()));
        return AuthResponse.from(user, token);
    }

    public MeResponse updateMe(UserPrincipal principal, UpdateMeRequest request) {
        UserEntity user = userRepository.findById(principal.getUser().getId())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        log.info("Попытка обновить профиль: userId={}", user.getId());

        String name = request.name();
        if (name != null) {
            name = name.trim();
            if (name.isEmpty()) {
                throw new BadRequestException("NAME_INVALID", "Имя не может быть пустым");
            }
            user.setName(name);
        }

        String surname = request.surname();
        if (surname != null) {
            surname = surname.trim();
            if (surname.isEmpty()) {
                throw new BadRequestException("SURNAME_INVALID", "Фамилия не может быть пустой");
            }
            user.setSurname(surname);
        }

        if (request.patronymic() != null) {
            user.setPatronymic(trimToNull(request.patronymic()));
        }

        String nextParentFullName = request.parentFullName() == null
                ? user.getParentFullName()
                : trimToNull(request.parentFullName());
        String nextParentPhone = request.parentPhone() == null
                ? user.getParentPhone()
                : normalizePhone(request.parentPhone());

        Integer age = user.getAge();
        if (age != null && age < 14) {
            if (nextParentFullName == null || nextParentPhone == null) {
                throw new BadRequestException(
                        "PARENT_REQUIRED",
                        "Если пользователю меньше 14 лет, нужно указать ФИО родителя и его номер телефона"
                );
            }
        }

        user.setParentFullName(nextParentFullName);
        user.setParentPhone(nextParentPhone);

        UserEntity saved = userRepository.save(user);
        log.info("Профиль обновлён: userId={}", saved.getId());
        return MeResponse.from(saved);
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
