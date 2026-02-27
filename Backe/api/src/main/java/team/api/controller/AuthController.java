package team.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.api.dto.request.LoginRequest;
import team.api.dto.request.RegisterRequest;
import team.api.dto.response.ApiResponse;
import team.api.dto.response.AuthResponse;
import team.api.dto.response.UserResponse;
import team.api.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Body: { username, email, password, fullName, phoneNumber }
     * → Trả về thông tin user vừa tạo (không có token, yêu cầu đăng nhập)
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse user = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đăng ký thành công!", user));
    }

    /**
     * POST /api/auth/login
     * Body: { username, password }
     * → Trả về JWT token + thông tin user
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Đăng nhập thành công!", authResponse));
    }
}