package team.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.api.dto.request.UpdateProfileRequest;
import team.api.dto.response.UserResponse;
import team.api.entity.User;
import team.api.exception.ResourceNotFoundException;
import team.api.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Lấy thông tin user hiện tại (dùng username từ JWT)
    public UserResponse getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        return UserResponse.fromEntity(user);
    }

    // Cập nhật thông tin cá nhân
    public UserResponse updateProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        return UserResponse.fromEntity(userRepository.save(user));
    }

    // ===== ADMIN ONLY =====

    // Lấy danh sách tất cả user
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    // Lấy user theo ID
    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id: " + id));
        return UserResponse.fromEntity(user);
    }

    // Đổi role của user (admin dùng)
    public UserResponse changeRole(Integer userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id: " + userId));

        try {
            User.Role newRole = User.Role.valueOf(roleName.toLowerCase());
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role không hợp lệ: " + roleName + ". Chỉ chấp nhận: admin, staff, customer");
        }

        return UserResponse.fromEntity(userRepository.save(user));
    }

    // Xóa user (admin dùng)
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Không tìm thấy user với id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
