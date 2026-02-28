package dev.ixlax.backend.auth.controller;

import dev.ixlax.backend.auth.request.LoginRequest;
import dev.ixlax.backend.auth.request.RegisterRequest;
import dev.ixlax.backend.auth.response.AuthResponse;
import dev.ixlax.backend.auth.response.MeResponse;
import dev.ixlax.backend.auth.service.AuthService;
import dev.ixlax.backend.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Регистрация")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Логин")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Текущий пользователь")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(MeResponse.from(principal));
    }
}
