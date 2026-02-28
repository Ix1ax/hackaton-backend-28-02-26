package dev.ixlax.backend.admin.controller;

import dev.ixlax.backend.admin.dto.AdminUpdateUserRequest;
import dev.ixlax.backend.admin.dto.AdminUserResponse;
import dev.ixlax.backend.admin.service.AdminUsersService;
import dev.ixlax.backend.common.dto.MessageResponse;
import dev.ixlax.backend.entities.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "Admin Users")
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    public AdminUsersController(AdminUsersService adminUsersService) {
        this.adminUsersService = adminUsersService;
    }

    @GetMapping
    @Operation(summary = "Список пользователей")
    public ResponseEntity<List<AdminUserResponse>> list() {
        List<AdminUserResponse> users = adminUsersService.listUsers().stream().map(AdminUserResponse::from).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя")
    public ResponseEntity<AdminUserResponse> get(@PathVariable long id) {
        UserEntity user = adminUsersService.getUser(id);
        return ResponseEntity.ok(AdminUserResponse.from(user));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Редактировать пользователя")
    public ResponseEntity<AdminUserResponse> update(@PathVariable long id, @Valid @RequestBody AdminUpdateUserRequest request) {
        UserEntity updated = adminUsersService.updateUser(id, request);
        return ResponseEntity.ok(AdminUserResponse.from(updated));
    }

    @PostMapping("/{id}/block")
    @Operation(summary = "Заблокировать пользователя")
    public ResponseEntity<AdminUserResponse> block(@PathVariable long id) {
        UserEntity updated = adminUsersService.blockUser(id);
        return ResponseEntity.ok(AdminUserResponse.from(updated));
    }

    @PostMapping("/{id}/unblock")
    @Operation(summary = "Разблокировать пользователя")
    public ResponseEntity<AdminUserResponse> unblock(@PathVariable long id) {
        UserEntity updated = adminUsersService.unblockUser(id);
        return ResponseEntity.ok(AdminUserResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    public ResponseEntity<MessageResponse> delete(@PathVariable long id) {
        adminUsersService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("200", "OK"));
    }
}

