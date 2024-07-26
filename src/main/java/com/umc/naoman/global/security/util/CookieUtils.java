package com.umc.naoman.global.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.web.util.WebUtils;

import java.io.Serializable;
import java.util.Base64;

public class CookieUtils {

    public static void addCookie(HttpServletResponse response, String name, String value,
                                 int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        // HTTPS 적용 시 함께 적용
        // cookie.setSecure(true);
        // cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String name) {
        Cookie targetCookie = WebUtils.getCookie(request, name);
        if (targetCookie != null) {
            Cookie cookie = new Cookie(targetCookie.getName(), null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    // 객체를 직렬화(Object -> String)해 쿠키 값으로 변환
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize((Serializable) obj));
    }

    // 쿠키를 역직렬화(String -> Object)해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
