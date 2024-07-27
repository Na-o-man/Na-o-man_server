package com.umc.naoman.global.security.util;

import com.umc.naoman.global.security.attribute.OAuthAttribute;
import com.umc.naoman.global.security.model.MemberDetails;
import com.umc.naoman.global.security.service.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {
    private final SecretKey secretKey;
    private final MemberDetailsService memberDetailsService;

    // "HMACSHA256" 알고리즘명을 저장
    private static final String SIGNATURE_ALGORITHM = Jwts.SIG.HS256.key().build().getAlgorithm();
    private static final String PAYLOAD_MEMBER_ID_KEY = "memberId";
    private static final String PAYLOAD_EMAIL_KEY = "email";
    private static final String PAYLOAD_ROLE_KEY = "role";

    JwtUtils(@Value("${jwt.secret}") String secret, MemberDetailsService memberDetailsService) {
        this.memberDetailsService = memberDetailsService;
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM);
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(PAYLOAD_EMAIL_KEY, String.class);
    }

    public Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration().before(new Date());
    }

    public String createJwt(String email, String role, Long seconds) {
        final LocalDateTime now = LocalDateTime.now();
        final Date issuedDate = localDateTimeToDate(now);
        final Date expiredDate = localDateTimeToDate(now.plusSeconds(seconds));

        return Jwts.builder()
                .claim(PAYLOAD_EMAIL_KEY, email)
                .claim(PAYLOAD_ROLE_KEY, role)
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public String createJwt(OAuthAttribute oAuthAttribute, Long seconds) {
        final LocalDateTime now = LocalDateTime.now();
        final Date issuedDate = localDateTimeToDate(now);
        final Date expiredDate = localDateTimeToDate(now.plusSeconds(seconds));

        return Jwts.builder()
                .claim(PAYLOAD_EMAIL_KEY, oAuthAttribute.getEmail())
                .claim("name", oAuthAttribute.getName())
                .claim("image", oAuthAttribute.getImage())
                .claim("socialType", oAuthAttribute.getProvider())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        // 나ㅇ만 서비스는 현재 Member 엔티티에게 권한이 존재하지 않으므로 authorities는 빈 리스트 처리
        final List<SimpleGrantedAuthority> authorities = Collections.emptyList();

        // 사용자 정의로 구현한 MemberDetails 사용
        final MemberDetails principal = memberDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
