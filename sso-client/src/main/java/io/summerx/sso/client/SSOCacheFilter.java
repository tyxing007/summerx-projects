package io.summerx.sso.client;

import io.summerx.framework.web.utils.CookieUtils;
import org.springframework.cache.Cache;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于Cache的SSO过滤器
 *
 * @author summerx
 * @Date 2016-07-12
 */
public class SSOCacheFilter extends AbstractSSOFilter {

    // 存放PT的Cookie名
    public static final String COOKIE_PT = "SSO_CLIENT_PT";

    // 存放用户信息的缓存
    private Cache userCache;

    protected Object getCurrentUserObject(HttpServletRequest request, HttpServletResponse response) {
        // 从Cookie中拿到Ticket
        String ticket = CookieUtils.getCookieValue(request, COOKIE_PT);
        if (ticket == null) {
            return null;
        } else {
            return userCache.get(ticket);
        }
    }

    protected void storeCurrentUserObject(HttpServletRequest request, HttpServletResponse response, Object userObject, int expires) {
        // 用户信息存入缓存（存入缓存的KEY和Cookie的值一致，这里直接将PT作为KEY值）
        userCache.put("userObject", userObject);
        // PT存入Cookie
        Cookie ticketCookie = CookieUtils.createCookie(COOKIE_PT, "userObject", null, request.getContextPath(), -1, false);
        CookieUtils.addCookie(response, ticketCookie);
    }

    public void setUserCache(Cache userCache) {
        this.userCache = userCache;
    }
}
