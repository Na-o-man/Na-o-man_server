package com.umc.naoman.global.security;

import com.umc.naoman.domain.member.repository.MemberRepository;
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
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final RefreshTokenService refreshTokenService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(jwtUtils, refreshTokenService,
                memberRepository, oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder amBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = amBuilder.build();

        http.authenticationManager(authenticationManager);

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .disable()
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/callback/oauth2/code/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/members/new").permitAll()
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
