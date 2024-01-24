package aladdinsys.api.jwt;

import aladdinsys.api.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final MemberRepository repo;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwtToken = extractJwtFromRequest(request);

            if (jwtToken != null && validateToken(jwtToken)) {
                Authentication authentication = getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String jwtToken = bearerToken.substring(7);
            System.out.println("Extracted JWT Token: " + jwtToken);
            return jwtToken;
        }

        return null;
    }

    private boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(jwtToken).getBody();
            System.out.println("Decoded JWT Claims: " + claims.toString());
            return true;
        } catch (SignatureException ex) {
            return false;
        }
    }

    private Authentication getAuthentication(String jwtToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(jwtToken).getBody();
        String userId = claims.getSubject();

        var member = repo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("조건을 만족하지 않습니다."));

        return new UsernamePasswordAuthenticationToken(userId, null,member.getRoles());
    }
}
