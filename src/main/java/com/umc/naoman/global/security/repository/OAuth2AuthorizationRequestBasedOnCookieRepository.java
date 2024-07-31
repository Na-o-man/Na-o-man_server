package com.umc.naoman.global.security.repository;

import com.umc.naoman.global.security.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.util.WebUtils;

@Slf4j
public class OAuth2AuthorizationRequestBasedOnCookieRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "OAuth2_AUTHORIZATION_REQUEST";
    private final static int AUTHORIZATION_REQUEST_COOKIE_EXPIRE_SECONDS = 1800;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String stateParameter = request.getParameter(OAuth2ParameterNames.STATE);
        if (stateParameter == null) {
            log.debug("loadAuthorizationRequest() - state 파라미터 부재");
            return null;
        }

        Cookie authorizationRequestCookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        if (authorizationRequestCookie != null) {
            OAuth2AuthorizationRequest authorizationRequest = CookieUtils.deserialize(authorizationRequestCookie,
                    OAuth2AuthorizationRequest.class);

            if (stateParameter.equals(authorizationRequest.getState())) {
                return authorizationRequest;
            } else {
                log.debug("loadAuthorizationRequest() - state 파라미터 불일치");
                return null;
            }
        } else {
            log.debug("loadAuthorizationRequest() - OAuth2_AUTHORIZATION_REQUEST 키의 쿠키 존재하지 않음");
            return null;
        }
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            log.debug("saveAuthorizationRequest() - 파라미터로 전달된 authorizationRequest가 null");
            return;
        }

        if (authorizationRequest.getState() == null) {
            log.debug("saveAuthorizationRequest() - authorization.getState() 값 null");
            throw new IllegalArgumentException("authorizationRequest.state cannot be empty");
        }
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtils.serialize(authorizationRequest), AUTHORIZATION_REQUEST_COOKIE_EXPIRE_SECONDS);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
        if (authorizationRequest != null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        } else {
            log.debug("removeAuthorizationRequest() - loadAuthorizationREquest() 리턴값 null");
        }
        return authorizationRequest;
    }
}