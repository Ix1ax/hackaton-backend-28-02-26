package dev.ixlax.backend.auth.service;

import dev.ixlax.backend.auth.exception.AuthException;
import dev.ixlax.backend.auth.request.LoginRequest;
import dev.ixlax.backend.auth.request.RegisterRequest;
import dev.ixlax.backend.auth.response.AuthResponse;
import dev.ixlax.backend.entities.UserEntity;
import dev.ixlax.backend.entities.UserRepository;
import dev.ixlax.backend.entities.UserRoleEnum;
import dev.ixlax.backend.security.JwtService;
import dev.ixlax.backend.utils.MessageException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
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
        if (userRepository.existsByEmail(email)) {
            throw new AuthException("Почта уже использует");
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setPatronymic(request.patronymic());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(UserRoleEnum.USER);

        UserEntity saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getEmail(), Map.of("uid", saved.getId()));
        return AuthResponse.from(saved, token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = formatEmail(request.email());
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Неправильная почта или пароль"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new AuthException("Неправильная почта или пароль");
        }

        String token = jwtService.generateToken(user.getEmail(), Map.of("uid", user.getId()));
        return AuthResponse.from(user, token);
    }

    private static String formatEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

}

