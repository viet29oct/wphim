package team.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.api.dto.request.LoginRequest;
import team.api.dto.request.RegisterRequest;
import team.api.dto.response.AuthResponse;
import team.api.dto.response.UserResponse;
import team.api.entity.User;
import team.api.repository.UserRepository;
import team.api.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ===== ĐĂNG KÝ =====
    public UserResponse register(RegisterRequest request) {

        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' đã được sử dụng");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' đã được sử dụng");
        }

        // Tạo user mới, mã hóa mật khẩu bằng BCrypt
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(User.Role.customer) // mặc định là customer
                .build();

        User saved = userRepository.save(user);
        return UserResponse.fromEntity(saved);
    }

    // ===== ĐĂNG NHẬP =====
    public AuthResponse login(LoginRequest request) {

        // Tìm user theo username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Username hoặc mật khẩu không đúng"));

        // So sánh mật khẩu người dùng nhập với hash trong DB
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Username hoặc mật khẩu không đúng");
        }

        // Tạo JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(UserResponse.fromEntity(user))
                .build();
    }
}
