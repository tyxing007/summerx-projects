package io.summerx.sso.client;

import io.summerx.framework.cache.CustomizedCache;
import io.summerx.framework.utils.RandomUtils;
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
    private CustomizedCache userCache;

    protected UserObject getCurrentUserObject(HttpServletRequest request, HttpServletResponse response) {
        // 从Cookie中拿到Ticket
        String ticket = CookieUtils.getCookieValue(request, COOKIE_PT);
        if (ticket == null) {
            return null;
        } else {
            return userCache.get(ticket, UserObject.class);
        }
    }

    protected void storeCurrentUserObject(HttpServletRequest request, HttpServletResponse response, UserObject userObject, int expires) {
        final String ticket = "PT_" + RandomUtils.generateAlphaNumberSequence(16);
        // 用户信息存入缓存（存入缓存的KEY和Cookie的值一致，这里直接将PT作为KEY值）
        userCache.put(ticket, userObject, expires);
        // PT存入Cookie
        Cookie ticketCookie = CookieUtils.createCookie(COOKIE_PT, ticket, null, request.getContextPath(), -1, false);
        CookieUtils.addCookie(response, ticketCookie, false);
    }

    public void setUserCache(CustomizedCache userCache) {
        this.userCache = userCache;
    }
}
