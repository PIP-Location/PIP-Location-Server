package kr.co.pinpick.common.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    // 암호화할 때 필요한 비밀 키(secret key)
    @Value("${jwt.secret}")
    private String secretKey;

    // 토큰 유효시간 1일
    @Value("${jwt.expiration_time}")
    private long accessTokenValidTime;

    private final UserDetailsServiceImpl userDetailsService;

    private JwtParser parser;

    // secretKey 객체 초기화, Base64로 인코딩
    @PostConstruct
    protected void init() {
        parser = Jwts.parser().setSigningKey(secretKey);
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenValidTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        var user = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String getUserPk(String token) {
        return parser.setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_JWT_EXCEPTION);
            log.info("Expired JWT Token", e);
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.MALFORMED_JWT_EXCEPTION);
            log.info("Invalid JWT Token", e);
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_JWT_EXCEPTION);
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
            log.info("JWT claims string is empty.", e);
        } catch (SignatureException e) {
            request.setAttribute("exception", ErrorCode.SIGNATURE_JWT_EXCEPTION);
            log.info("Modulated JWT Token", e);
        }
        return false;
    }
}