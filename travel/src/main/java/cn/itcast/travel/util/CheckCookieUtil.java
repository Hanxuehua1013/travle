package cn.itcast.travel.util;

import javax.servlet.http.Cookie;

public class CheckCookieUtil {
    public static Cookie findCookie(Cookie[] cookies, String cookieName) {

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
}