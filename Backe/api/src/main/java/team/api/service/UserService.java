package team.api.service;

import org.springframework.stereotype.Service;
import team.api.dto.request.UpdateProfileRequest;
import team.api.dto.response.UserResponse;
import team.api.entity.User;
import team.api.exception.ResourceNotFoundException;
import team.api.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        return UserResponse.fromEntity(user);
    }

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

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id: " + id));
        return UserResponse.fromEntity(user);
    }

    public UserResponse changeRole(Integer userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id: " + userId));
        try {
            User.Role newRole = User.Role.valueOf(roleName.toLowerCase());
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role không hợp lệ: " + roleName);
        }
        return UserResponse.fromEntity(userRepository.save(user));
    }

    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Không tìm thấy user với id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
