package team.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import team.api.dto.request.UpdateProfileRequest;
import team.api.dto.response.ApiResponse;
import team.api.dto.response.UserResponse;
import team.api.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET /api/users/me
     * Header: Authorization: Bearer <token>
     * → Lấy thông tin user đang đăng nhập
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(Principal principal) {
        // principal.getName() = username lấy từ JWT trong filter
        UserResponse user = userService.getMyProfile(principal.getName());
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin thành công", user));
    }

    /**
     * PUT /api/users/me
     * Header: Authorization: Bearer <token>
     * Body: { fullName, phoneNumber }
     * → Cập nhật thông tin cá nhân
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            Principal principal,
            @Valid @RequestBody UpdateProfileRequest request) {

        UserResponse updated = userService.updateProfile(principal.getName(), request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thành công", updated));
    }

    // ===== ADMIN ENDPOINTS =====

    /**
     * GET /api/users
     * Chỉ ADMIN được truy cập
     * → Lấy danh sách tất cả user
     */
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách thành công", users));
    }

    /**
     * GET /api/users/{id}
     * Chỉ ADMIN được truy cập
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin thành công", user));
    }

    /**
     * PUT /api/users/{id}/role?role=admin
     * Chỉ ADMIN được đổi role của user khác
     */
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<UserResponse>> changeRole(
            @PathVariable Integer id,
            @RequestParam String role) {

        UserResponse updated = userService.changeRole(id, role);
        return ResponseEntity.ok(ApiResponse.ok("Đổi role thành công", updated));
    }

    /**
     * DELETE /api/users/{id}
     * Chỉ ADMIN được xóa user
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa user thành công", null));
    }
}