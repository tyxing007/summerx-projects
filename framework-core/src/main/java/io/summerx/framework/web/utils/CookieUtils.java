package io.summerx.framework.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    /* 浏览器关闭时自动删除 */
    public final static int CLEAR_BROWSER_IS_CLOSED = -1;

    /* 立即删除 */
    public final static int CLEAR_IMMEDIATELY_REMOVE = 0;

    public static final String DEFAULT_COOKIE_PATH = "/";

    public static void addCookie(HttpServletResponse response, Cookie cookie) {
        addCookie(response, cookie, false);
    }
    public static void addCookie(HttpServletResponse response, Cookie cookie, boolean httpOnly) {
        response.addCookie(cookie);
        if (httpOnly) {
            response.addHeader("Set-Cookie", "HttpOnly");
        }
    }

    public static Cookie createCookie(String name, String value, String domain, String path, int expiry, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null && !"".equals(domain)) {
            cookie.setDomain(domain);
        }
        if (path != null && !"".equals(path)) {
            cookie.setPath(path);
        } else {
            cookie.setPath(DEFAULT_COOKIE_PATH);
        }
        cookie.setMaxAge(expiry);
        cookie.setSecure(secure);
        return cookie;
    }

    public static Cookie createCookie(String name, String value, String domain, String path) {
        return createCookie(name, value, domain, path, CLEAR_BROWSER_IS_CLOSED, false);
    }
    public static Cookie createCookie(String name, String value) {
        return createCookie(name, value, null, DEFAULT_COOKIE_PATH);
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        if (cookieName == null) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = getCookie(request, cookieName);
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }

    public static void removeCookie(String cookieName, HttpServletResponse response) {
        if (cookieName == null) {
            return;
        }
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath(DEFAULT_COOKIE_PATH);
        cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
        response.addCookie(cookie);
    }
}