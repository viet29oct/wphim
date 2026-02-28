package team.api.dto.response;

public class AuthResponse {
    private String token;
    private String tokenType;
    private UserResponse user;

    public AuthResponse() {}
    public AuthResponse(String token, String tokenType, UserResponse user) {
        this.token = token;
        this.tokenType = tokenType;
        this.user = user;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String token;
        private String tokenType;
        private UserResponse user;
        public Builder token(String v) { this.token = v; return this; }
        public Builder tokenType(String v) { this.tokenType = v; return this; }
        public Builder user(UserResponse v) { this.user = v; return this; }
        public AuthResponse build() { return new AuthResponse(token, tokenType, user); }
    }
}
