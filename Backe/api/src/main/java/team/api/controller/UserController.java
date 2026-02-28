package team.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.api.dto.request.UpdateProfileRequest;
import team.api.dto.response.ApiResponse;
import team.api.dto.response.UserResponse;
import team.api.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(Principal principal) {
        UserResponse user = userService.getMyProfile(principal.getName());
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin thành công", user));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            Principal principal,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserResponse updated = userService.updateProfile(principal.getName(), request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thành công", updated));
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách thành công", users));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin thành công", user));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<UserResponse>> changeRole(
            @PathVariable Integer id,
            @RequestParam String role) {
        UserResponse updated = userService.changeRole(id, role);
        return ResponseEntity.ok(ApiResponse.ok("Đổi role thành công", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa user thành công", null));
    }
}
