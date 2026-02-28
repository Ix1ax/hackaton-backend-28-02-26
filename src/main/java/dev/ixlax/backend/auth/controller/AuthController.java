package dev.ixlax.backend.auth.controller;

import dev.ixlax.backend.auth.request.LoginRequest;
import dev.ixlax.backend.auth.request.RegisterRequest;
import dev.ixlax.backend.auth.request.UpdateMeRequest;
import dev.ixlax.backend.auth.response.AuthResponse;
import dev.ixlax.backend.auth.response.MeResponse;
import dev.ixlax.backend.auth.service.AuthService;
import dev.ixlax.backend.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация", security = {})
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Логин", security = {})
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Текущий пользователь")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(MeResponse.from(principal));
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Обновить профиль",
            description = "Можно менять имя/фамилию/отчество и данные родителя. Нельзя менять email и дату рождения/возраст."
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<MeResponse> updateMe(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateMeRequest request
    ) {
        return ResponseEntity.ok(authService.updateMe(principal, request));
    }
}
