package team.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // Lấy từ application.yaml: jwt.secret và jwt.expiration
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs; // milliseconds, ví dụ 86400000 = 1 ngày

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Tạo token từ username
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)                      // nhúng role vào token
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    // Lấy username từ token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Lấy role từ token
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Kiểm tra token còn hợp lệ không
    public boolean isTokenValid(String token) {
        try {
            getClaims(token); // nếu hết hạn hoặc sai chữ ký → throw exception
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}