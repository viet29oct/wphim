package team.api.dto.response;

import team.api.entity.User;
import java.time.LocalDateTime;

public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String role;
    private LocalDateTime createdAt;

    public UserResponse() {}
    public UserResponse(Integer userId, String username, String email, String fullName,
                        String phoneNumber, String role, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getPhoneNumber(),
            user.getRole().name(),
            user.getCreatedAt()
        );
    }

    // Getters & Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder (kept for compatibility)
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Integer userId;
        private String username, email, fullName, phoneNumber, role;
        private LocalDateTime createdAt;
        public Builder userId(Integer v) { this.userId = v; return this; }
        public Builder username(String v) { this.username = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder fullName(String v) { this.fullName = v; return this; }
        public Builder phoneNumber(String v) { this.phoneNumber = v; return this; }
        public Builder role(String v) { this.role = v; return this; }
        public Builder createdAt(LocalDateTime v) { this.createdAt = v; return this; }
        public UserResponse build() {
            return new UserResponse(userId, username, email, fullName, phoneNumber, role, createdAt);
        }
    }
}
