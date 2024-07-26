package com.umc.naoman.global.security.filter;

import com.umc.naoman.global.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_TYPE = "Bearer ";
    private final JwtUtils jwtUtils;
    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (!validateJwtIsPresent(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorization.substring(AUTHORIZATION_TYPE.length());
        System.out.println("jwt: " + jwt);
        if (jwtUtils.isExpired(jwt)) {
            System.out.println("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = jwtUtils.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean validateJwtIsPresent(String authorization) {
        if (authorization == null || !authorization.startsWith(AUTHORIZATION_TYPE)) {
            System.out.println("토큰이 존재하지 않거나, 인증 타입이 Bearer가 아닙니다.");
            return false;
        }

        return true;
    }
}
