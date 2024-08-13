package com.umc.naoman.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.naoman.global.error.ErrorCode;
import com.umc.naoman.global.error.ErrorResponse;
import com.umc.naoman.global.error.code.JwtErrorCode;
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
                         AuthenticationException authException) throws IOException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute("authException");
        if (errorCode == null) {
            errorCode = UNAUTHORIZED;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus());
        response.setCharacterEncoding(Charset.defaultCharset().name());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(response.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}