package com.umc.naoman.global.security;

import com.umc.naoman.domain.member.service.MemberService;
import com.umc.naoman.domain.member.service.redis.RefreshTokenService;
import com.umc.naoman.global.security.filter.JwtAuthenticationFilter;
import com.umc.naoman.global.security.handler.CustomAccessDeniedHandler;
import com.umc.naoman.global.security.handler.OAuth2LoginSuccessHandler;
import com.umc.naoman.global.security.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.umc.naoman.global.security.service.CustomOAuth2UserService;
import com.umc.naoman.global.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberService memberService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final RefreshTokenService refreshTokenService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtUtils jwtUtils;

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(jwtUtils, refreshTokenService, memberService,
                oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .disable()
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler())
                        .loginPage("/auth/login")
                )
                .addFilterAfter(new JwtAuthenticationFilter(jwtUtils), OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }
}
