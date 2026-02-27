package team.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Nếu không có token thì bỏ qua, tiếp tục chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Tách token ra khỏi "Bearer <token>"
        String token = authHeader.substring(7);

        // 4. Validate token
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Lấy username và role từ token
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        // 6. Set authentication vào SecurityContext
        //    Dùng ROLE_ prefix theo convention của Spring Security
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
        var authentication = new UsernamePasswordAuthenticationToken(
                username, null, authorities
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}