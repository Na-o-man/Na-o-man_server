package com.umc.naoman.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.naoman.global.error.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.umc.naoman.global.error.code.GlobalErrorCode.UNAUTHORIZED;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.getStatus());
        response.setCharacterEncoding(Charset.defaultCharset().name());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(response.getStatus())
                .code(UNAUTHORIZED.getMessage())
                .message(authException.getMessage())
                .data(null)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}